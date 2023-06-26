package com.dcns.dailycost.data

import androidx.compose.runtime.Composable
import com.dcns.dailycost.R

enum class CategoryIcon {
    Ads,
    Pet,
    Bus,
    Bill,
    Coin,
    Game,
    Shop,
    Other,
    Award,
    Salary,
    Coffee,
    Mobile,
    Bitcoin,
    Blogger,
    CardCoin,
    Electronic,
    Investment,
    MoneyReceive,
    ShoppingCart,
    Entertainment,
    MonitorMobile,
    BitcoinConvert;

    val iconResId: Int
        @Composable
        get() = when (this) {
            Ads -> R.drawable.ic_ads
            Pet -> R.drawable.ic_coin
            Bus -> R.drawable.ic_bus
            Bill -> R.drawable.ic_pet
            Coin -> R.drawable.ic_bill
            Game -> R.drawable.ic_game
            Shop -> R.drawable.ic_shop
            Other -> R.drawable.ic_other
            Award -> R.drawable.ic_award
            Salary -> R.drawable.ic_salary
            Coffee -> R.drawable.ic_coffee
            Mobile -> R.drawable.ic_mobile
            Bitcoin -> R.drawable.ic_bitcoin
            Blogger -> R.drawable.ic_blogger
            CardCoin -> R.drawable.ic_card_coin
            Electronic -> R.drawable.ic_electronic
            Investment -> R.drawable.ic_investment
            MoneyReceive -> R.drawable.ic_money_receive
            ShoppingCart -> R.drawable.ic_shopping_cart
            Entertainment -> R.drawable.ic_entertainment
            MonitorMobile -> R.drawable.ic_monitor_mobile
            BitcoinConvert -> R.drawable.ic_bitcoin_convert
        }
}