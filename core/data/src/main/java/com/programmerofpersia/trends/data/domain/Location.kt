package com.programmerofpersia.trends.data.domain

sealed class Location(val title: String, val geo: String) {

    object Argentina : Location("Argentina", "")
    object Australia : Location("Australia", "")
    object Austria : Location("Austria", "")
    object Belgium : Location("Belgium", "")
    object Brazil : Location("Brazil", "")
    object Canada : Location("Canada", "")
    object Chile : Location("Chile", "")
    object Colombia : Location("Colombia", "")
    object Czechia : Location("Czechia", "")
    object Denmark : Location("Denmark", "")
    object Egypt : Location("Egypt", "")
    object Finland : Location("Finland", "")
    object France : Location("France", "")
    object Germany : Location("Germany", "")
    object Greece : Location("Greece", "")
    object HongKong : Location("Hong Kong", "")
    object Hungary : Location("Hungary", "")
    object India : Location("India", "")
    object Indonesia : Location("Indonesia", "")
    object Ireland : Location("Ireland", "")
    object Italy : Location("Italy", "")
    object Japan : Location("Japan", "")
    object Kenya : Location("Kenya", "")
    object Malaysia : Location("Malaysia", "")
    object Mexico : Location("Mexico", "")
    object Netherlands : Location("Netherlands", "")
    object NewZealand : Location("New Zealand", "")
    object Nigeria : Location("Nigeria", "")
    object Norway : Location("Norway", "")
    object Peru : Location("Peru", "")
    object Philippines : Location("Philippines", "")
    object Poland : Location("Poland", "")
    object Portugal : Location("Portugal", "")
    object Romania : Location("Romania", "")
    object Russia : Location("Russia", "")
    object SaudiArabia : Location("Saudi Arabia", "")
    object Singapore : Location("Singapore", "")
    object SouthAfrica : Location("South Africa", "")
    object SouthKorea : Location("South Korea", "")
    object Spain : Location("Spain", "")
    object Sweden : Location("Sweden", "")
    object Switzerland : Location("Switzerland", "")
    object Taiwan : Location("Taiwan", "")
    object Thailand : Location("Thailand", "")
    object Türkiye : Location("Türkiye", "")
    object Ukraine : Location("Ukraine", "")
    object UnitedKingdom : Location("United Kingdom", "")
    object UnitedStates : Location("United States", "US")
    object Vietnam : Location("Vietnam", "")


}
