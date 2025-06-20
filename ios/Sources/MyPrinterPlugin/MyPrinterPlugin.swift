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

        @objc func printBlob(_ call: CAPPluginCall) {
                guard let base64Data = call.getString("data"),
                      let type = call.getString("type") else {
                    call.reject("Data and type ('image' or 'pdf') are required for printing blob.")
                    return
                }

                guard let decodedData = Data(base64Encoded: base64Data) else {
                    call.reject("Failed to decode base64 data.")
                    return
                }

                let jobName = call.getString("jobName") ?? "My Document"

                DispatchQueue.main.async {
                    self.printController = UIPrintInteractionController.shared

                    let printInfo = UIPrintInfo.printInfo()
                    printInfo.jobName = jobName

                    if type == "image" {
                        printInfo.outputType = .photo
                        self.printController?.printingItem = UIImage(data: decodedData) // Direct image printing
                    } else if type == "pdf" {
                        printInfo.outputType = .general
                        self.printController?.printingItem = decodedData // Direct PDF data printing
                    } else {
                        call.reject("Unsupported blob type: \(type)")
                        self.printController = nil
                        return
                    }

                    self.printController?.printInfo = printInfo

                    guard let viewController = self.bridge?.viewController else {
                        call.reject("Could not get a valid UIViewController for presenting print dialog.")
                        self.printController = nil
                        return
                    }

                    self.printController?.present(animated: true, completionHandler: { (controller, completed, error) in
                        if completed {
                            call.resolve()
                        } else if let error = error {
                            call.reject("Printing failed: \(error.localizedDescription)")
                        } else {
                            call.reject("Printing cancelled.")
                        }
                        self.printController = nil
                    })
                }
        }
}
