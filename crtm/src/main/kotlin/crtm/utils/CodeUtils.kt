package crtm.utils

//TODO Only works for codMode 8 aka interurban
fun createLineCode(codMode: String, lineCode: String) = "${codMode}__${lineCode}___"
fun createStopCode(codMode: String, stopCode: String) = "${codMode}_${stopCode}"
fun getCodModeFromLineCode(input: String): String = input.substringBefore("__")
