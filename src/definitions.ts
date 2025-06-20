export interface MyPrinterPlugin {
  print(options: { html: string; jobName?: string }): Promise<void>;
  printBlob(options: { data: Blob; type: 'image' | 'pdf'; jobName?: string }): Promise<void>;
}
