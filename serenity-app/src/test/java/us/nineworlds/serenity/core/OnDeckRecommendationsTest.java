/**
 * The MIT License (MIT)
 * Copyright (c) 2014 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class OnDeckRecommendationsTest {

	/**
	 *
	 */
	private static final String ANDROID_SOFTWARE_LEANBACK = "android.software.leanback";
	/**
	 *
	 */
	private static final String ANDROID_HARDWARE_TYPE_TELEVISION = "android.hardware.type.television";
	private OnDeckRecommendations onDeckRecommendations;

	@Before
	public void setUp() {
		onDeckRecommendations = new OnDeckRecommendations(
				Robolectric.application);
	}

	@After
	public void tearDown() {

	}

	@Test
	@Config(emulateSdk = 18, reportSdk = 14)
	public void recommendationsDoNotOccurForVersionBelowJellyBean() {
		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isFalse();
	}

	@Test
	@Config(emulateSdk = 18, reportSdk = 17)
	public void recommendationsOccurForJellyBeanOrHigher() {
		RobolectricPackageManager pm = (RobolectricPackageManager) Robolectric.application
				.getPackageManager();
		pm.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		pm.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, true);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(Robolectric.application);
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, true);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isTrue();
	}

	@Test
	@Config(emulateSdk = 18, reportSdk = 17)
	public void recommendationsOccurForJellyBeanOrHigherFailOnGoogleTV4DevicesWithOutLeanback() {
		RobolectricPackageManager pm = (RobolectricPackageManager) Robolectric.application
				.getPackageManager();
		pm.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		pm.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, false);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(Robolectric.application);
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, true);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isFalse();
	}

	@Test
	@Config(emulateSdk = 18, reportSdk = 17)
	public void recommendationsOccurForJellyBeanOrHigherFailWhenAndroidTVModeIsFalse() {
		RobolectricPackageManager pm = (RobolectricPackageManager) Robolectric.application
				.getPackageManager();
		pm.setSystemFeature(ANDROID_HARDWARE_TYPE_TELEVISION, true);
		pm.setSystemFeature(ANDROID_SOFTWARE_LEANBACK, true);

		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(Robolectric.application);
		Editor edit = prefrences.edit();
		edit.putBoolean(SerenityConstants.PREFERENCE_ONDECK_RECOMMENDATIONS,
				true);
		edit.putBoolean(SerenityConstants.PREFERENCE_TV_MODE, false);
		edit.apply();

		boolean result = onDeckRecommendations.recommended();
		assertThat(result).isFalse();
	}

}
