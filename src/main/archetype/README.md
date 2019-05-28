# Sample AEM CIF project template

This is a project template for AEM-based CIF applications. It is intended as a best-practice set of examples as well as a potential starting point to develop your own CIF-based commerce store.

## Modules

The main parts of the template are:

* all: All-in-one package that contains additional dependencies like the CIF and WCM core components, GraphQL tooling and CIF connector.
* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.apps: contains the /apps (and /etc) parts of the project, ie. JavaScript and CSS clientlibs, components, templates and runmode specific configs.
* ui.content: contains content using the components from the ui.apps

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

If you have a running AEM instance you can build and package the whole project and deploy into AEM with

    mvn clean install -PautoInstallAll

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallAllPublish

Or alternatively

    mvn clean install -PautoInstallAll -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html