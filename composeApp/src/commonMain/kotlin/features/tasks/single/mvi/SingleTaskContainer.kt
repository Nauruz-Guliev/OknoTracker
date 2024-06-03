package features.tasks.single.mvi

import extensions.isPastTime
import flow_mvi.ConfigurationFactory
import flow_mvi.configure
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pro.respawn.flowmvi.api.Container
import pro.respawn.flowmvi.api.FlowMVIDSL
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
import utils.validation.InputField
import utils.validation.Validator
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SingleTaskContainer(
    private val errorMapper: ErrorMapper,
    private val repository: TaskRepository,
    private val configurationFactory: ConfigurationFactory,
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
                action(SingleTaskAction.ShowSnackbar(errorMapper.map(exception = exception).title))
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
                        intent.taskId?.let {
                            launch { attachmentRepository.updateAttachments(intent.taskId) }
                            handleNetworkRequest {
                                repository.getTask(intent.taskId)
                            } ?: TaskModel()
                        }.also {
                            updateState {
                                SingleTaskState.Success(
                                    it ?: TaskModel(),
                                    emptyList()
                                )
                            }
                        }
                    }

                    is SingleTaskIntent.OnFileSelected -> {
                        handleNetworkRequest {
                            val bytes = intent.file?.readBytes()
                            if (bytes != null) {
                                val image = attachmentRepository.saveAttachment(
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
                                withState {
                                }
                            }
                        }
                    }

                    is SingleTaskIntent.DeleteAttachment -> {
                        handleNetworkRequest {
                            attachmentRepository.deleteAttachment(intent.id)
                            tryUpdateState { model ->
                                model?.copy(attachments = model.attachments.filter { it.id != intent.id })
                            }
                        }
                    }

                    is SingleTaskIntent.OnDateSelected -> {
                        val date = intent.date.toLocalDateTime(TimeZone.UTC)
                        if (date.isPastTime()) {
                            action(SingleTaskAction.ShowSnackbar("Past time"))
                        } else {
                            tryUpdateState { model ->
                                model?.copy(deadlineTime = date.toString())
                            }
                        }
                    }

                    is SingleTaskIntent.OnPrioritySelected -> {
                        tryUpdateState { model ->
                            model?.copy(priority = intent.priority.name)
                        }
                    }
                }
            }
        }
    @FlowMVIDSL
    private fun PipelineContext<SingleTaskState, *, *>.tryUpdateState(
        block: (TaskModel?) -> TaskModel?,
    ) {
        useState {
            if (this is SingleTaskState.Success) {
                block(this.taskModel)?.let {
                    SingleTaskState.Success(
                        it,
                        emptyList()
                    )
                } ?: this
            } else {
                this
            }
        }
    }

    private suspend fun <T> PipelineContext<SingleTaskState, *, SingleTaskAction>.handleNetworkRequest(
        block: suspend () -> T,
    ): T? {
        return try {
            block()
        } catch (ex: Exception) {
            action(SingleTaskAction.ShowSnackbar(ex.message.toString()))
            null
        }
    }

    private suspend fun PipelineContext<SingleTaskState, SingleTaskIntent, SingleTaskAction>.handleTaskChange(
        taskModel: TaskModel,
        block: suspend () -> Unit,
    ) {
        val isNameValid = nameValidator.validate(taskModel.name)
        val isDescriptionValid =
            taskModel.description?.let { descriptionValidator.validate(it) } ?: false

        when {
            isNameValid && isDescriptionValid -> {
                block()
            }
            else -> {
                withState {
                    val fieldErrors = buildList {
                        if (!isNameValid) {
                            add(InputField.NAME)
                        }
                        if (!isDescriptionValid) {
                            add(InputField.DESCRIPTION)
                        }
                    }
                    if (this is SingleTaskState.Success) {
                        SingleTaskState.ValidationError(taskModel, fieldErrors)
                    }
                }
            }
        }
    }
}

