package com.example.main.configuration.preferences;

import com.example.main.service.controls.ControlType;


public class ControlsData {

  public int up;

  public int down;

  public int left;

  public int right;

  public int jump;

  public ControlType type;

  public int index;

  public boolean invertX, invertY, invertXY;

  public ControlsData() {
  }

  public ControlsData(final ControlType type) {
    this.type = type;
  }
}
