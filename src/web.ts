import { WebPlugin } from '@capacitor/core';

import type { MyPrinterPlugin } from './definitions';

export class MyPrinterWeb extends WebPlugin implements MyPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
