package com.example.landmarkindonesia

object LandmarkData {
    private val landmarkNames = arrayOf(
        "Monumen Nasional",
        "Tugu Yogyakarta")
    private val landmarkDetails = arrayOf(
        "Monumen Nasional adalah monumen peringatan setinggi 132 meter (433 kaki) yang didirikan untuk mengenang perlawanan dan perjuangan rakyat Indonesia untuk merebut kemerdekaan dari pemerintahan kolonial Hindia Belanda.",
        "Tugu Yogyakarta adalah sebuah tugu atau monumen yang sering dipakai sebagai simbol atau lambang dari kota Yogyakarta.")
    private val landmarkImage = intArrayOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground)

    val listData: ArrayList<LandmarkModel>
        get(){
            val list = arrayListOf<LandmarkModel>()
            for(position in landmarkNames.indices){
                val landmark = LandmarkModel()
                landmark.name = landmarkNames[position]
                landmark.detail = landmarkDetails[position]
                landmark.photo = landmarkImage[position]

                list.add(landmark)
            }
            return list
        }
}