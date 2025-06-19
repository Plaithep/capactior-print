import Foundation

@objc public class MyPrinter: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
