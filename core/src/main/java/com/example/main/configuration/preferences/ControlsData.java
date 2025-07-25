package com.example.main.configuration.preferences;

import com.example.main.service.controls.ControlType;

/** JSON-encoded class. Uses public fields to support libGDX JSON utilities. */
public class ControlsData {
  /** Up movement shortcut. */
  public int up;
  /** Down movement shortcut. */
  public int down;
  /** Left movement shortcut. */
  public int left;
  /** Right movement shortcut. */
  public int right;
  /** Jump shortcut. */
  public int jump;
  /** Type of controls */
  public ControlType type;
  /** Additional data. Might be used for device ID. */
  public int index;
  /** Optional settings. Might not be supported by every controller. */
  public boolean invertX, invertY, invertXY;

  public ControlsData() {
  }

  public ControlsData(final ControlType type) {
    this.type = type;
  }
}