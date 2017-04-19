package io.takari.builder.internal.model;

import io.takari.builder.GeneratedSourcesDirectory;

public class GeneratedSourcesDirectoryParameter
    extends AbstractFileParameter<GeneratedSourcesDirectory> {

  public GeneratedSourcesDirectoryParameter(MemberAdapter element, TypeAdapter type) {
    super(element, type, GeneratedSourcesDirectory.class);
  }

  @Override
  public String[] value() {
    return annotation.value();
  }

  @Override
  public String[] defaultValue() {
    return annotation.defaultValue();
  }

  @Override
  public boolean required() {
    return annotation.required();
  }

  @Override
  public void accept(BuilderMetadataVisitor visitor) {
    visitor.visitGeneratedSourcesDirectory(this);
  }
}
