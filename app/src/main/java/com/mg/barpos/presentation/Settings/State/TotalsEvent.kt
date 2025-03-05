package com.mg.barpos.presentation.Settings.State

sealed interface TotalsEvent {
    data class PrintTotals(
        val print: Boolean
    ) : TotalsEvent

    data class DeleteOrders(
        val delete: Boolean
    ) : TotalsEvent
}