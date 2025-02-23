package com.mg.barpos.utils

import android.content.Context
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo

class PrintAdapter (
    val context: Context
) : PrintDocumentAdapter() {
    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        // Calculate the layout of the document and provide it to the callback
        // This is where you'd determine the number of pages, etc.
        val info = PrintDocumentInfo.Builder("document.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(1) // Replace with actual page count
            .build()

        callback.onLayoutFinished(info, newAttributes != oldAttributes)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {
        // Write the actual document content to the provided file descriptor
        // This is where you'd generate PDF content or any other printable format
        try {
            // Write your PDF generation logic here
            // You might use a library like iText or PdfDocument
            callback.onWriteFinished(pages)
        } catch (e: Exception) { callback.onWriteFailed(e.message)
        }
    }
}