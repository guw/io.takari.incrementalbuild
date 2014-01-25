package io.takari.incremental.internal;

import io.takari.incremental.BuildContext;

import java.io.File;
import java.io.Serializable;

public class DefaultInput implements BuildContext.Input<File> {

  private transient final BuildContextStateManager state;

  private final File file;

  public DefaultInput(BuildContextStateManager context, File file) {
    this.state = context;
    this.file = file;
  }

  @Override
  public void associateIncludedInput(File file) {
    state.associateIncludedInput(this, file);
  }

  @Override
  public DefaultOutput associateOutput(File file) {
    return state.associateOutput(this, file);
  }

  public void associateOutput(DefaultOutput output) {
    state.associate(this, output);
  }

  public void addRequirement(String qualifier, String localName) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isProcessingRequired() {
    return state.isProcessingRequired(this);
  }

  @Override
  public File getResource() {
    return file;
  }

  @Override
  public <T extends Serializable> void setValue(String key, T value) {
    // TODO Auto-generated method stub

  }

  @Override
  public <T extends Serializable> T getValue(String key, Class<T> clazz) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addMessage(int line, int column, String message, int severity, Throwable cause) {
    // TODO Auto-generated method stub

  }

  public boolean isAssociatedOutput(File file) {
    return state.isAssociatedOutput(this, file);
  }

  @Override
  public int hashCode() {
    return file.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof DefaultInput)) {
      return false;
    }

    DefaultInput other = (DefaultInput) obj;

    // must be from the same context to be equal
    return state == other.state && file.equals(other.file);
  }
}
