package karldrabek.goobaapp.goobaapp.ui.utils

import platform.Foundation.NSDateFormatter
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIDatePicker
import platform.UIKit.UIDatePickerMode
import platform.UIKit.UIDatePickerStyle

/**
 * IOS-specific implementation of [karldrabek.goobaapp.goobaapp.ui.utils.pickTime]
 *
 *  @param context in this case this will be null and will not be used. it is an artifact of the interface allowing for Android context
 *  @param onTimePicked when the time is selected it calls onTimePicked with the current time in HH:MM
 */
actual fun pickTime(
    context: Any?,
    onTimePicked: (String) -> Unit,
) {
    val timePicker =
        UIDatePicker().apply {
            datePickerMode = UIDatePickerMode.UIDatePickerModeTime
            preferredDatePickerStyle =
                UIDatePickerStyle.UIDatePickerStyleWheels /** Direct wheel-style picker */
            translatesAutoresizingMaskIntoConstraints = false
        }

    val alertController =
        UIAlertController.alertControllerWithTitle(
            title = "Pick a Time",
            message = "\n\n\n\n\n\n\n",
            preferredStyle = UIAlertControllerStyleAlert,
        )

    alertController.view.addSubview(timePicker)

    timePicker.centerXAnchor.constraintEqualToAnchor(alertController.view.centerXAnchor).active =
        true
    timePicker.topAnchor
        .constraintEqualToAnchor(
            alertController.view.topAnchor,
            constant = 10.0,
        ).active = true
    timePicker.widthAnchor
        .constraintEqualToAnchor(
            alertController.view.widthAnchor,
            constant = 0.0,
        ).active = true

    /** Add a "Done" button */
    alertController.addAction(
        UIAlertAction.actionWithTitle(
            title = "Done",
            style = UIAlertActionStyleDefault,
        ) { _ ->
            val formatter =
                NSDateFormatter().apply {
                    dateFormat = "HH:mm"
                }
            val selectedTime = formatter.stringFromDate(timePicker.date)
            onTimePicked(selectedTime)
        },
    )

    /** Present the alert */
    UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
        alertController,
        animated = true,
        completion = null,
    )
}
