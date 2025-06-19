import { WebPlugin } from '@capacitor/core';

import type { MyPrinterPlugin } from './definitions';

export class MyPrinterWeb extends WebPlugin implements MyPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async print(options: { html: string; jobName?: string }): Promise<void> {
    console.warn('Printing is not supported on the web platform using this plugin.');
    // You could, as a fallback, use your original iframe logic here for desktop web browsers.
    // const iframe = document.createElement('iframe');
    // document.body.appendChild(iframe);
    // const printer = iframe.contentWindow;
    // ... (your iframe content setup)
    // printer.print();
    // iframe.remove();
    alert('Printing is not available in the web browser.');
    return Promise.resolve(); // Resolve to avoid errors, but it didn't print.
  }
}
