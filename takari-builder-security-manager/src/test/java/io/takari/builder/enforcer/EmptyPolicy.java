package io.takari.builder.enforcer;

import io.takari.builder.enforcer.Policy;

public class EmptyPolicy implements Policy {

  @Override
  public void checkSocketPermission() {

  }

  @Override
  public void checkPropertyPermission(String action, String name) {

  }

  @Override
  public void checkExec(String cmd) {

  }

  @Override
  public void checkRead(String file) {

  }

  @Override
  public void checkWrite(String file) {

  }

}
