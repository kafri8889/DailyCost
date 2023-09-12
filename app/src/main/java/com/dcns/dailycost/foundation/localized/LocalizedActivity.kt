package com.dcns.dailycost.foundation.localized

import android.content.Context
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dcns.dailycost.data.Language
import com.dcns.dailycost.foundation.localized.data.OnLocaleChangedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale

abstract class LocalizedActivity: AppCompatActivity() {

	private val localizedViewModel: LocalizedViewModel by viewModels()
	private var currentLocale: Locale? = null

	private var listener: OnLocaleChangedListener? = null

	private val _language = MutableStateFlow(Language.English)
	val language: StateFlow<Language> = _language

	init {
		lifecycleScope.launch {
			lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
				localizedViewModel.uiEvent.collect { event ->
					when (event) {
						is LocalizedUiEvent.LanguageChanged -> {
							_language.update { event.language }
						}

						is LocalizedUiEvent.ApplyLanguage -> {
							val newLocale = Locale(event.language.lang)

							if (newLocale.language != currentLocale?.language) {
								currentLocale = newLocale
								LocalizationUtil.applyLanguageContext(
									context = this@LocalizedActivity,
									locale = this@LocalizedActivity.getLocale()
								)

								listener?.onChanged()
							}
						}
					}
				}
			}
		}
	}

	override fun getApplicationContext(): Context {
		val context = super.getApplicationContext()
		return LocalizationUtil.applyLanguageContext(context, context.getLocale())
	}

	override fun getBaseContext(): Context {
		val context = super.getBaseContext()
		return LocalizationUtil.applyLanguageContext(context, context.getLocale())
	}

	override fun attachBaseContext(newBase: Context) {
		super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, newBase.getLocale()))
	}

	private fun Context.getLocale(): Locale {
		if (currentLocale == null) {
			runBlocking {
				language
					.take(1)
					.collect { language ->
						currentLocale = Locale(language.lang)
					}
			}
		}

		return currentLocale!!
	}

	fun setOnLocaleChangedListener(mListener: OnLocaleChangedListener) {
		this.listener = mListener
	}

	fun removeOnLocaleChangedListener() {
		this.listener = null
	}

	fun setLanguage(language: Language) {
		localizedViewModel.onAction(
			LocalizedAction.SetLanguage(language)
		)
	}

}