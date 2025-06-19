# capacitor-webview-printer 

A custom Capacitor plugin providing native print functionality, allowing your web application to send HTML strings directly to the device's print manager on iOS (AirPrint) and Android (Print Spooler).

## Install

```bash
npm install custom-printer-plugin
npx cap sync
```

## API

<docgen-index>

* [`print(...)`](#print)

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

</docgen-api>
