package features.tasks.single

import features.fileds.InputField
import features.fileds.Validator
import flow_mvi.DefaultConfigurationFactory
import flow_mvi.configure
import kotlinx.coroutines.launch
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.PipelineContext
import pro.respawn.flowmvi.api.Store
import pro.respawn.flowmvi.dsl.store
import pro.respawn.flowmvi.plugins.recover
import pro.respawn.flowmvi.plugins.reduce
import ru.kpfu.itis.common.mapper.ErrorMapper
import ru.kpfu.itis.features.attachments.repository.AttachmentRepository
import ru.kpfu.itis.features.task.data.repository.TaskRepository
import ru.kpfu.itis.features.task.data.store.UserStore
import ru.kpfu.itis.features.task.domain.model.AttachmentModel
import ru.kpfu.itis.features.task.domain.model.TaskModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SingleTaskContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: DefaultConfigurationFactory,
    private val userStore: UserStore,
    private val attachmentRepository: AttachmentRepository,
    private val nameValidator: Validator,
    private val descriptionValidator: Validator,
) : Container<SingleTaskState, SingleTaskIntent, SingleTaskAction> {

    @OptIn(ExperimentalEncodingApi::class)
    override val store: Store<SingleTaskState, SingleTaskIntent, SingleTaskAction> =
        store(SingleTaskState.Initial) {

            configure(configurationFactory, "SingleTask")

            recover { exception ->
                updateState {
                    SingleTaskState.Error.Internal(errorMapper.map(exception = exception))
                }
                null
            }

            reduce { intent ->
                val userId = userStore.getUserId()!!

                when (intent) {
                    is SingleTaskIntent.CreateNew -> {
                        handleNetworkRequest {
                            handleTaskChange(intent.model) {
                                repository.createTask(intent.model.copy(userId = userId))
                                action(SingleTaskAction.CloseBottomSheet)
                            }
                        }
                    }

                    is SingleTaskIntent.Edit -> {
                        handleNetworkRequest {
                            handleTaskChange(intent.model) {
                                repository.changeTask(intent.model.copy(userId = userId))
                                action(SingleTaskAction.CloseBottomSheet)
                            }
                        }
                    }

                    is SingleTaskIntent.LoadTask -> {
                        updateState { SingleTaskState.Loading }
                        if (intent.taskId != null) {
                            launch { attachmentRepository.updateAttachments(intent.taskId) }
                            handleNetworkRequest {
                                repository.getTask(intent.taskId).also {
                                    updateState { SingleTaskState.Success(it) }
                                }
                            }
                        } else {
                            updateState { SingleTaskState.Success(TaskModel()) }
                        }
                    }

                    is SingleTaskIntent.OnFileSelected -> {
                        handleNetworkRequest {
                            val bytes = intent.file?.readBytes()
                            if (bytes != null) {
                                val savedImage = attachmentRepository.saveAttachment(
                                    AttachmentModel(
                                        name = intent.file.name,
                                        contentType = intent.file.name.substring(
                                            intent.file.name.lastIndexOf(
                                                "."
                                            ) + 1, intent.file.name.length
                                        ),
                                        taskId = intent.taskId,
                                        content = Base64.encode(bytes)
                                    )
                                )
                                action(
                                    SingleTaskAction.AddSelectedImage(
                                        ImageModel(
                                            bytes,
                                            savedImage.id
                                        )
                                    )
                                )
                            }
                        }
                    }

                    is SingleTaskIntent.UiError -> {
                        action(SingleTaskAction.ShowSnackbar(intent.message))
                    }

                    is SingleTaskIntent.DeleteAttachment -> {
                        handleNetworkRequest {
                            attachmentRepository.deleteAttachment(intent.id)
                            action(SingleTaskAction.DeleteAttachment(intent.id))
                        }
                    }
                }
            }
        }

    private suspend fun PipelineContext<SingleTaskState, SingleTaskIntent, SingleTaskAction>.handleNetworkRequest(
        block: suspend () -> Unit
    ) {
        runCatching {
            block()
        }.onFailure {
            updateState { SingleTaskState.Error.Server(errorMapper.map(exception = it)) }
        }
    }

    private suspend fun PipelineContext<SingleTaskState, SingleTaskIntent, SingleTaskAction>.handleTaskChange(
        taskModel: TaskModel,
        block: suspend () -> Unit
    ) {
        val isNameValid = nameValidator.validate(taskModel.name)
        val isDescriptionValid =
            taskModel.description?.let { descriptionValidator.validate(it) } ?: false

        when {
            isNameValid && isDescriptionValid -> {
                block()
            }

            else -> {
                updateState {
                    val fieldErrors = buildList {
                        if (!isNameValid) {
                            add(InputField.NAME)
                        }
                        if (!isDescriptionValid) {
                            add(InputField.DESCRIPTION)
                        }
                    }
                    SingleTaskState.Error.Validation(fieldErrors)
                }
            }
        }
    }
}

