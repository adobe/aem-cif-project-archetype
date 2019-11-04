[![CircleCI](https://circleci.com/gh/adobe/aem-cif-project-archetype.svg?style=svg)](https://circleci.com/gh/adobe/aem-cif-project-archetype)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.adobe.commerce.cif/cif-project-archetype/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.adobe.commerce.cif/cif-project-archetype)
![GitHub](https://img.shields.io/github/license/adobe/aem-cif-project-archetype.svg)

# CIF Project Archetype

This archetype creates a minimal Adobe Experience Manager CIF project as a starting point for your own projects using [CIF core components](https://github.com/adobe/aem-core-cif-components). The properties that must be provided when using this archetype allow to name as desired all parts of this project.

This project is based on [aem-project-archetype](https://github.com/adobe/aem-project-archetype).

## Provided Maven profiles

The generated maven project supports different deployment profiles when running the Maven install goal `mvn install` within the reactor.

| Id                        | Description                                                                                                                                                                                                                                                   |
| ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| autoInstallBundle         | Install core bundle with the maven-sling-plugin to the Felix console                                                                                                                                                                                          |
| autoInstallPackage        | Install the ui.content and ui.apps content package with the content-package-maven-plugin to the package manager to default author instance on localhost, port 4502. Hostname and port can be changed with the aem.host and aem.port user-defined properties.  |
| autoInstallPackagePublish | Install the ui.content and ui.apps content package with the content-package-maven-plugin to the package manager to default publish instance on localhost, port 4503. Hostname and port can be changed with the aem.host and aem.port user-defined properties. |

## Usage

To use a released version of this archetype use the following Maven command:

```bash
mvn archetype:generate \
    -DarchetypeGroupId=com.adobe.commerce.cif \
    -DarchetypeArtifactId=cif-project-archetype \
    -DarchetypeVersion=x.y.z
```

Where `x.y.z` is the archetype version number that you want to use (see archetype [versions](VERSIONS.md)). By default, generated projects depend on the [CIF connector](https://github.com/adobe/commerce-cif-connector). So make sure you have a compatible version (see [requirements](#requirements) below) installed on your AEM instance. 

For on-prem installations only, we recommend to embed the CIF connector into your project. This can be achieved by generating a project using the `-DoptionEmbedConnector=y` option.

### Available properties

| Name                  | Default | Description                        |
| --------------------- | ------- | ---------------------------------- |
| groupId               |         | Base Maven groupId                 |
| artifactId            |         | Base Maven ArtifactId              |
| version               |         | Version                            |
| package               |         | Java Source Package                |
| appsFolderName        |         | /apps folder name                  |
| artifactName          |         | Maven Project Name                 |
| componentGroupName    |         | AEM component group name           |
| contentFolderName     |         | /content folder name               |
| confFolderName        |         | /conf folder name                  |
| packageGroup          |         | Content Package Group name         |
| siteName              |         | AEM site name                      |
| optionAemVersion      | 6.5.0   | Target AEM version                 |
| optionIncludeExamples | y       | Include sample content package     |
| optionEmbedConnector  | n       | Embed CIF connector in all package |

Note: If the archetype is executed in interactive mode the first time properties with default values can't be changed (see
[ARCHETYPE-308](https://issues.apache.org/jira/browse/ARCHETYPE-308) for more details). The value can be changed when the property
confirmation at the end is denied and the questionnaire gets repeated or by passing the parameter in the command line (e.g.
`-DoptionIncludeExamples=n`).

### Requirements

The latest version of the AEM CIF Project Archetype, requires the below minimum system requirements:

| Archetype | AEM 6.4 | AEM 6.5 | Magento                   | CIF Connector | Java |
| --------- | ------- | ------- | ------------------------- | ------------- | ---- |
| 0.5.0     | 6.4.4.0 | 6.5.0   | 2.3.2<sup>*</sup> / 2.3.3 | 0.6.0         | 1.8  |
| 0.4.0     | 6.4.4.0 | 6.5.0   | 2.3.2<sup>*</sup>         | 0.5.0         | 1.8  |
| 0.3.0     | 6.4.4.0 | 6.5.0   | 2.3.1 / 2.3.2             | 0.4.0         | 1.8  |
| 0.2.0     | 6.4.4.0 | 6.5.0   | 2.3.1 / 2.3.2             | 0.3.0         | 1.8  |

- Apache Maven (3.3.9 or newer)
- Adobe Public Maven Repository in maven settings, see [Knowledge Base](https://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html) article for details.

<sup>*</sup> With version 0.4.0 we drop support for Magento 2.3.1 and only support 2.3.2. For version 2.3.2 we currently requiere the following 4 Magento GraphQL patches ([#758](https://github.com/magento/graphql-ce/issues/758), [#665](https://github.com/magento/graphql-ce/pull/665), [#666](https://github.com/magento/graphql-ce/pull/666), [#906](https://github.com/magento/graphql-ce/pull/906)) installed on the Magento instance. Most of them will be included in upcomming Magento 2.3.3. release.

For a list of requirements for previous versions, see [Historical System Requirements](VERSIONS.md).

## Building

To compile and use an edge, local version of this archetype:

```bash
mvn clean install
```

Then change to the directory in which you want to create the project and run:

```bash
mvn archetype:generate \
    -DarchetypeGroupId=com.adobe.commerce.cif \
    -DarchetypeArtifactId=cif-project-archetype \
    -DarchetypeVersion=x.y.z-SNAPSHOT
```

Side note: The profile "adobe-public" must be activated when using profiles like "autoInstallPackage" mentioned above.

## Releases to Maven Central

Releases of this project are triggered by manually running `mvn release:prepare release:clean` on the `master` branch on the root folder of this repository. Once you choose the release and the next snapshot versions, this commits the change along with a release git tag like for example `cif-project-archetype-x.y.z`. Note that the commits are not automatically pushed to the git repository, so you have some time to check your changes and then manually push them. The push then triggers a dedicated `CircleCI` build that performs the deployment of the tagged artifact to Maven Central.

## Demo Project

For demo purposes, we generate a sample store-front project for the latest commit on the `master` branch. The package includes all required dependencies including the [CIF connector](https://github.com/adobe/commerce-cif-connector). You can download it in the [release](https://github.com/adobe/aem-cif-project-archetype/releases/tag/latest) section and directly install it on your AEM instance.

The store-front requires an AEM dispatcher with forwarding rules specific to your Magento setup. Please follow the steps as described in the dispatcher [documentation](https://github.com/adobe/aem-core-cif-components/tree/master/dispatcher).

For more information about how to configure the sample project, please refer to the CIF core components [wiki](https://github.com/adobe/aem-core-cif-components/wiki/configuration).
