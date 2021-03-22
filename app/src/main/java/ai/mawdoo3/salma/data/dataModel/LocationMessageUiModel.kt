package ai.mawdoo3.salma.data.dataModel

/**
 * created by Omar Qadomi on 3/17/21
 */
data class LocationMessageUiModel(
    val name: String?,
    val details: String?,
    val workingHours: String?,
    val geofence: String?,
    val phone: String?,
    val messageSender: MessageSender
) :
    MessageUiModel {
    override var sender = messageSender

}