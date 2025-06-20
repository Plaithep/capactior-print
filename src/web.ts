import { WebPlugin } from '@capacitor/core';

import type { MyPrinterPlugin } from './definitions';

export class MyPrinterWeb extends WebPlugin implements MyPrinterPlugin {
  async print(): Promise<void> {
    console.warn('Printing is not supported on the web platform using this plugin.');
    alert('Printing is not available in the web browser.');

    return Promise.resolve(); // Resolve to avoid errors, but it didn't print.
  }

  async printBlob(options: { data: Blob; type: 'image' | 'pdf'; jobName?: string }): Promise<void> {
    console.warn(`Blob printing (type: ${options.type}) is not supported on the web platform.`);
    alert(`Blob Printing is not available in the web browser.`);

    return Promise.resolve();
  }
}
