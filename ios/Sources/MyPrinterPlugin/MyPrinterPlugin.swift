import Foundation
import Capacitor
import UIKit // For UIPrintInteractionController
import WebKit // For converting HTML to printable content

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(MyPrinterPlugin)
public class MyPrinterPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "MyPrinterPlugin"
    public let jsName = "MyPrinter"
    private let implementation = MyPrinter()

     @objc func print(_ call: CAPPluginCall) {
            guard let html = call.getString("html") else {
                call.reject("Must provide HTML content to print.")
                return
            }

            let jobName = call.getString("jobName") ?? "My Document"

            DispatchQueue.main.async {
                let printInfo = UIPrintInfo.printInfo()
                printInfo.outputType = .general // .general for documents, .photo for photos
                printInfo.jobName = jobName

                // Use UIMarkupTextPrintFormatter for HTML
                let formatter = UIMarkupTextPrintFormatter(markupText: html)

                let printController = UIPrintInteractionController.shared
                printController.printInfo = printInfo
                printController.printFormatter = formatter

                // Present the print dialog
                printController.present(animated: true) { (controller, completed, error) in
                    if completed {
                        call.resolve()
                    } else if let error = error {
                        call.reject("Printing failed: \(error.localizedDescription)")
                    } else {
                        call.reject("Printing cancelled.")
                    }
                }
            }
        }
}
