export interface MyPrinterPlugin {
  print(options: { html: string; jobName?: string }): Promise<void>;
}
