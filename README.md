# capacitor-webview-printer 

A custom Capacitor plugin providing native print functionality, allowing your web application to send HTML and Blob strings directly to the device's print manager on iOS (AirPrint) and Android (Print Spooler).

## Install

```bash
npm install capacitor-webview-printer 
npx cap sync
```

## API

<docgen-index>

* [`print(...)`](#print)
* [`printBlob(...)`](#printblob)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### print(...)

```typescript
print(options: { html: string; jobName?: string; }) => Promise<void>
```

| Param         | Type                                             |
| ------------- | ------------------------------------------------ |
| **`options`** | <code>{ html: string; jobName?: string; }</code> |

--------------------


### printBlob(...)

```typescript
printBlob(options: { data: Blob; type: 'image' | 'pdf'; jobName?: string; }) => Promise<void>
```

| Param         | Type                                                                  |
| ------------- | --------------------------------------------------------------------- |
| **`options`** | <code>{ data: any; type: 'image' \| 'pdf'; jobName?: string; }</code> |

--------------------

</docgen-api>
