export interface MyPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
