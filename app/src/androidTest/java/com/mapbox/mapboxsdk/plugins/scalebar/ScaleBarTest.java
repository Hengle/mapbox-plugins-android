package com.mapbox.mapboxsdk.plugins.scalebar;


import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.util.DisplayMetrics;
import android.view.View;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.plugins.BaseActivityTest;
import com.mapbox.mapboxsdk.plugins.testapp.R;
import com.mapbox.mapboxsdk.plugins.testapp.activity.TestActivity;
import com.mapbox.pluginscalebar.ScaleBarManager;
import com.mapbox.pluginscalebar.ScaleBarOption;
import com.mapbox.pluginscalebar.ScaleBarWidget;

import org.junit.Test;
import org.junit.runner.RunWith;

import timber.log.Timber;

import static com.mapbox.mapboxsdk.plugins.annotation.MapboxMapAction.invoke;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Basic smoke tests for ScaleBar
 */
@RunWith(AndroidJUnit4.class)
public class ScaleBarTest extends BaseActivityTest {
  private ScaleBarManager scaleBarManager;
  private Activity activity;
  private ScaleBarWidget scaleBarWidget;

  @Override
  protected Class getActivityClass() {
    return TestActivity.class;
  }

  private void setupScaleBar() {
    Timber.i("Retrieving layer");
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      scaleBarManager = new ScaleBarManager(idlingResource.getMapView(), mapboxMap);
      activity = rule.getActivity();
      scaleBarWidget = scaleBarManager.create(new ScaleBarOption(activity));
      assertNotNull(scaleBarManager);
      assertNotNull(scaleBarWidget);
    });
  }

  @Test
  public void testScaleBarEnable() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(View.VISIBLE, scaleBarWidget.getVisibility());
      assertTrue(scaleBarManager.isEnabled());
      scaleBarManager.setEnabled(false);
      assertEquals(View.GONE, scaleBarWidget.getVisibility());
      assertFalse(scaleBarManager.isEnabled());
    });
  }

  @Test
  public void testScaleBarColor() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getTextColor());
      assertEquals(activity.getResources().getColor(android.R.color.black), scaleBarWidget.getPrimaryColor());
      assertEquals(activity.getResources().getColor(android.R.color.white), scaleBarWidget.getSecondaryColor());


      int textColor = R.color.colorAccent;
      int colorPrimary = R.color.colorPrimary;
      int colorSecondary = R.color.colorPrimaryDark;

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setTextColor(textColor);
      option.setPrimaryColor(colorPrimary);
      option.setSecondaryColor(colorSecondary);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(activity.getResources().getColor(textColor), scaleBarWidget.getTextColor());
      assertEquals(activity.getResources().getColor(colorPrimary), scaleBarWidget.getPrimaryColor());
      assertEquals(activity.getResources().getColor(colorSecondary), scaleBarWidget.getSecondaryColor());
    });
  }

  @Test
  public void testScaleBarWidth() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(MapView.class, scaleBarWidget.getParent().getClass());
      MapView parent = (MapView) scaleBarWidget.getParent();
      assertEquals(parent.getWidth(), scaleBarWidget.getMapViewWidth());
    });
  }

  @Test
  public void testMargin() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_margin_left)),
        scaleBarWidget.getMarginLeft(), 0);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_margin_top)),
        scaleBarWidget.getMarginTop(), 0);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_text_margin)),
        scaleBarWidget.getTextBarMargin(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setMarginLeft(R.dimen.fab_margin);
      option.setMarginTop(R.dimen.fab_margin);
      option.setTextBarMargin(R.dimen.fab_margin);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getMarginLeft(), 0);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getMarginTop(), 0);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getTextBarMargin(), 0);

      option = new ScaleBarOption(activity);
      option.setMarginLeft(100f);
      option.setMarginTop(50f);
      option.setTextBarMargin(30f);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(100f, scaleBarWidget.getMarginLeft(), 0);
      assertEquals(50f, scaleBarWidget.getMarginTop(), 0);
      assertEquals(30f, scaleBarWidget.getTextBarMargin(), 0);

    });
  }

  @Test
  public void testBarHeight() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_height)),
        scaleBarWidget.getBarHeight(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setBarHeight(R.dimen.fab_margin);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getBarHeight(), 0);

      option = new ScaleBarOption(activity);
      option.setBarHeight(100f);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(100f, scaleBarWidget.getBarHeight(), 0);

    });
  }

  @Test
  public void testTextSize() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_text_size)),
        scaleBarWidget.getTextSize(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setTextSize(R.dimen.fab_margin);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getTextSize(), 0);

      option = new ScaleBarOption(activity);
      option.setTextSize(100f);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(100f, scaleBarWidget.getTextSize(), 0);

    });
  }

  @Test
  public void testBorderWidth() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.mapbox_scale_bar_border_width)),
        scaleBarWidget.getBorderWidth(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setBorderWidth(R.dimen.fab_margin);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(convertDpToPixel(activity.getResources().getDimension(R.dimen.fab_margin)),
        scaleBarWidget.getBorderWidth(), 0);

      option = new ScaleBarOption(activity);
      option.setBorderWidth(100f);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(100f, scaleBarWidget.getBorderWidth(), 0);

    });
  }


  @Test
  public void testRefreshInterval() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(ScaleBarOption.REFRESH_INTERVAL_DEFAULT, scaleBarWidget.getRefreshInterval(), 0);

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setRefreshInterval(1000);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertEquals(1000, scaleBarWidget.getRefreshInterval(), 0);

    });
  }

  @Test
  public void testMetrics() {
    validateTestSetup();
    setupScaleBar();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      assertEquals(ScaleBarOption.LocaleUnitResolver.isMetricSystem(), scaleBarWidget.isMetricUnit());

      ScaleBarOption option = new ScaleBarOption(activity);
      option.setMetricUnit(true);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertTrue(scaleBarWidget.isMetricUnit());

      option = new ScaleBarOption(activity);
      option.setMetricUnit(false);
      scaleBarWidget = scaleBarManager.create(option);
      assertNotNull(scaleBarWidget);
      assertFalse(scaleBarWidget.isMetricUnit());
    });
  }


  private float convertDpToPixel(float dp) {
    return dp * ((float) activity.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}