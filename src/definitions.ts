export interface MyPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  print(options: { html: string; jobName?: string }): Promise<void>;
}
