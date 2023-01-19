package com.teleferik.ui.privateTrip

class ExpandableListDataPump {
    companion object {
        fun getData(): Map<String, List<String>> {
            val hashMap = mutableMapOf<String, List<String>>()

            val cairoList = mutableListOf<String>()
            cairoList.add("a")
            cairoList.add("b")
            cairoList.add("c")

            val alexList = mutableListOf<String>()
            alexList.add("d")
            alexList.add("e")
            alexList.add("f")

            val bnsList = mutableListOf<String>()
            bnsList.add("g")
            bnsList.add("h")
            bnsList.add("i")


            hashMap["cairo"] = cairoList
            hashMap["Alexandria"] = alexList
            hashMap["Beni Suef"] = bnsList


            return hashMap
        }
    }
}