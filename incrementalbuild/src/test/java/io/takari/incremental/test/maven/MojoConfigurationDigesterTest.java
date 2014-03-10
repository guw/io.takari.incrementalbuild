package io.takari.incremental.test.maven;

import io.takari.incrementalbuild.maven.internal.MojoConfigurationDigester;
import io.takari.incrementalbuild.maven.testing.IncrementalBuildRule;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MojoConfigurationDigesterTest {

  @Rule
  public final TestResources resources = new TestResources();

  @Rule
  public final IncrementalBuildRule mojos = new IncrementalBuildRule();

  private File basedir;

  private MavenProject project;

  private MavenSession session;

  @Before
  public void setUp() throws Exception {
    basedir = resources.getBasedir("digester");
    project = mojos.readMavenProject(basedir);
    session = mojos.newMavenSession(project);
  }

  private Xpp3Dom newParameter(String name, String value) {
    Xpp3Dom parameter = new Xpp3Dom(name);
    parameter.setValue(value);
    return parameter;
  }

  private Map<String, Serializable> digest(Xpp3Dom... parameters) throws Exception {

    PluginDescriptor plugin = new PluginDescriptor();
    plugin.setArtifacts(Collections.<Artifact>emptyList());
    MojoDescriptor mojo = new MojoDescriptor();
    mojo.setPluginDescriptor(plugin);
    mojo.setImplementationClass(DigestedMojo.class);
    MojoExecution execution = new MojoExecution(mojo);

    Xpp3Dom configuration = new Xpp3Dom("configuration");
    for (Xpp3Dom parameter : parameters) {
      configuration.addChild(parameter);
    }
    execution.setConfiguration(configuration);

    return new MojoConfigurationDigester(session, project, execution).digest();
  }

  @Test
  public void testBasic() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("string", "string"));
    Assert.assertEquals("string", digest.get("mojo.parameter.string"));
  }

  @Test
  public void testIgnored() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("ignored", "ignored"));
    Assert.assertNull(digest.get("mojo.parameter.ignored"));
  }

  @Test
  public void testNotIgnored() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("notignored", "notignored"));
    Assert.assertEquals("notignored", digest.get("mojo.parameter.notignored"));
  }

  public void testProject() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("project", "${project}"));
    Assert.assertNotNull(digest.get("project"));
  }

  @Test
  public void testReferencedObject() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("output", "${project.build.directory}"));
    Assert.assertEquals(project.getBuild().getDirectory(), digest.get("mojo.parameter.output"));
  }

  @Test
  public void testNotAParameter() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("unparameter", "string"));
    Assert.assertNull(digest.get("mojo.parameter.unparameter"));
  }

  @Test
  public void testXmlParameterName() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("camel-case", "string"));
    Assert.assertEquals("string", digest.get("mojo.parameter.camelCase"));
  }

  @Test
  public void testLocalRepository() throws Exception {
    MavenArtifactRepository localrepo = new MavenArtifactRepository();
    localrepo.setUrl("file:///local/repo/url");
    session.getRequest().setLocalRepository(localrepo);
    Map<String, Serializable> digest = digest(newParameter("localrepo", "${localRepository}"));
    Assert.assertEquals(localrepo.getUrl(), digest.get("mojo.parameter.localrepo"));
  }

  @Test
  public void testArtifactRepositoryCollection() throws Exception {
    Map<String, Serializable> digest =
        digest(newParameter("remotes", "${project.remoteArtifactRepositories}"));
    Assert.assertNotNull("string", digest.get("mojo.parameter.remotes"));
  }

  @Test
  public void testArtifactCollection() throws Exception {
    Map<String, Serializable> digest = digest(newParameter("dependencies", "${project.artifacts}"));
    Assert.assertNotNull(digest.get("mojo.parameter.dependencies"));
  }
}