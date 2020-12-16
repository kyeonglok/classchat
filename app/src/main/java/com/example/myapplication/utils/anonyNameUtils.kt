package com.example.myapplication.utils

object anonyNameUtils{
    val nameList = arrayListOf<String>(
            "김병지","홍명보","호날두활배근","멧시","지상렬","차범근","박주영","차두리","빅뱅대성","염기훈","음바페","콩국수","김찬호","마루쉐","엄준식","동대문시장","좌변기","피자스쿨","코딱지","일진출신"
    ,"3대500단속반","길고양이수호자","수능갤러리삼수생","절대강자","층간소음마스터","트월킹머신","잔반처리반","관짝춤머신","영양갱절도범","이발병","시흥시청공익","사재기빌런","푸드파이터","비트코인왕","개미핥기조련사",
    "잼민이담당일진","청주의딸","에버랜드술고래","미리보기사이트업로더","클럽문지기","진상손님","비트코인추종자","에어팟하수구에빠진놈","현행범","한석원","공문서위조왕","소리없는방귀빌런","분필셔틀","먹방비제이","탈모인협회장","테이저건테러범")
    fun getAnonyName(uid:String,classid:String) : String?{
        val p = 131
        val m = 51

        val hashInput = uid+classid
        var hash_val = 0
        var pow_p = 1

        for(c in hashInput){
            hash_val = (hash_val+(c-'a'+1)*pow_p)%m
            pow_p = (p*pow_p)%m
        }
        return nameList[hash_val]
    }
}