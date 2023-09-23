package com.dcns.dailycost.data

import androidx.compose.runtime.Composable
import com.dcns.dailycost.R

enum class CategoryIcon {
	Airplane,
	Armchair,
	Baby,
	Barbel,
	Basket,
	Bed,
	Bitcoin,
	Blogger,
	BookOpen,
	BowlFood,
	Brain,
	Brandy,
	Buildings,
	Bus,
	Cake,
	CallBell,
	Car,
	CarProfile,
	CardCoin,
	CardHolder,
	Cat,
	ChalkboardSimple,
	ChartLine,
	ChartLineUp,
	ChartPie,
	CodeSimple,
	Coffee,
	Coin,
	Coins,
	CurrencyDollar,
	DesktopTower,
	Download,
	FileText,
	FirstAidKit,
	Flash,
	ForkKnife,
	GameBoy,
	GasPump,
	Gift,
	Graph,
	Hammer,
	HandCoins,
	Headphone,
	Heart,
	House,
	Key,
	LightBulbFilament,
	Money,
	Monitor,
	Motorcycle,
	Other,
	Palette,
	Park,
	PayPal,
	Person,
	Pet,
	Phone,
	PiggyBank,
	Popcorn,
	PuzzlePiece,
	RocketLaunch,
	Rug,
	Ship,
	ShirtFolded,
	ShoppingBagOpen,
	ShoppingCartSimple,
	SketchLogo,
	SteeringWheel,
	Stethoscope,
	Student,
	SuitcaseSimple,
	TShirt,
	Tag,
	Taxi,
	Teacher,
	Ticket,
	Tram,
	Tree,
	Trophy,
	TruckFast,
	Van,
	VinylRecord,
	Wallet,
	Watch,
	WifiMedium,
	Wrench,
	Youtube;

	val iconResId: Int
		@Composable
		get() = when (this) {
			Airplane -> R.drawable.ic_airplane
			Armchair -> R.drawable.ic_armchair
			Baby -> R.drawable.ic_baby
			Barbel -> R.drawable.ic_barbell
			Basket -> R.drawable.ic_basket
			Bed -> R.drawable.ic_bed
			Bitcoin -> R.drawable.ic_bitcoin
			Blogger -> R.drawable.ic_blogger
			BookOpen -> R.drawable.ic_book_open
			BowlFood -> R.drawable.ic_bowl_food
			Brain -> R.drawable.ic_brain
			Brandy -> R.drawable.ic_brandy
			Buildings -> R.drawable.ic_buildings
			Bus -> R.drawable.ic_bus
			Cake -> R.drawable.ic_cake
			CallBell -> R.drawable.ic_call_bell
			Car -> R.drawable.ic_car
			CarProfile -> R.drawable.ic_car_profile
			CardCoin -> R.drawable.ic_card_coin
			CardHolder -> R.drawable.ic_cardholder
			Cat -> R.drawable.ic_cat
			ChalkboardSimple -> R.drawable.ic_chalkboard_simple
			ChartLine -> R.drawable.ic_chart_line
			ChartLineUp -> R.drawable.ic_chart_line_up
			ChartPie -> R.drawable.ic_chart_pie
			CodeSimple -> R.drawable.ic_code_simple
			Coffee -> R.drawable.ic_coffee
			Coin -> R.drawable.ic_coin
			Coins -> R.drawable.ic_coins
			CurrencyDollar -> R.drawable.ic_currency_dollar
			DesktopTower -> R.drawable.ic_desktop_tower
			Download -> R.drawable.ic_download
			FileText -> R.drawable.ic_file_text
			FirstAidKit -> R.drawable.ic_first_aid_kit
			Flash -> R.drawable.ic_flash
			ForkKnife -> R.drawable.ic_fork_knife
			GameBoy -> R.drawable.ic_gameboy
			GasPump -> R.drawable.ic_gas_pump
			Gift -> R.drawable.ic_gift
			Graph -> R.drawable.ic_graph
			Hammer -> R.drawable.ic_hammer
			HandCoins -> R.drawable.ic_hand_coins
			Headphone -> R.drawable.ic_headphone
			Heart -> R.drawable.ic_heart
			House -> R.drawable.ic_house
			Key -> R.drawable.ic_key
			LightBulbFilament -> R.drawable.ic_lightbulb_filament
			Money -> R.drawable.ic_money
			Monitor -> R.drawable.ic_monitor
			Motorcycle -> R.drawable.ic_motorcycle
			Other -> R.drawable.ic_other
			Palette -> R.drawable.ic_palette
			Park -> R.drawable.ic_park
			PayPal -> R.drawable.ic_paypal
			Person -> R.drawable.ic_person
			Pet -> R.drawable.ic_pet
			Phone -> R.drawable.ic_phone
			PiggyBank -> R.drawable.ic_piggy_bank
			Popcorn -> R.drawable.ic_popcorn
			PuzzlePiece -> R.drawable.ic_puzzle_piece
			RocketLaunch -> R.drawable.ic_rocket_launch
			Rug -> R.drawable.ic_rug
			Ship -> R.drawable.ic_ship
			ShirtFolded -> R.drawable.ic_shirt_folded
			ShoppingBagOpen -> R.drawable.ic_shopping_bag_open
			ShoppingCartSimple -> R.drawable.ic_shopping_cart_simple
			SketchLogo -> R.drawable.ic_sketch_logo
			SteeringWheel -> R.drawable.ic_steering_wheel
			Stethoscope -> R.drawable.ic_stethoscope
			Student -> R.drawable.ic_student
			SuitcaseSimple -> R.drawable.ic_suitcase_simple
			TShirt -> R.drawable.ic_t_shirt
			Tag -> R.drawable.ic_tag
			Taxi -> R.drawable.ic_taxi
			Teacher -> R.drawable.ic_teacher
			Ticket -> R.drawable.ic_ticket
			Tram -> R.drawable.ic_tram
			Tree -> R.drawable.ic_tree
			Trophy -> R.drawable.ic_trophy
			TruckFast -> R.drawable.ic_truck_fast
			Van -> R.drawable.ic_van
			VinylRecord -> R.drawable.ic_vinyl_record
			Wallet -> R.drawable.ic_wallet
			Watch -> R.drawable.ic_watch
			WifiMedium -> R.drawable.ic_wifi_medium
			Wrench -> R.drawable.ic_wrench
			Youtube -> R.drawable.ic_youtube
		}
}