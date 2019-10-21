/*******************************************************************************
 *
 *    Copyright 2019 Adobe. All rights reserved.
 *    This file is licensed to you under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License. You may obtain a copy
 *    of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software distributed under
 *    the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
 *    OF ANY KIND, either express or implied. See the License for the specific language
 *    governing permissions and limitations under the License.
 *
 ******************************************************************************/

'use strict';

const ci = new (require('./ci.js'))();
const fs = require('fs');

ci.context();

ci.stage('Install Archetype');
ci.sh('mvn -B clean install');


ci.stage('Generate Sample Project');
const releaseVersion = ci.sh(`mvn help:evaluate -Dexpression=project.version -q -DforceStdout`, true).toString().trim();
ci.sh('mkdir -p venia-store');
ci.dir('venia-store', () => {
    ci.sh(`mvn archetype:generate -B \
        -DarchetypeGroupId="com.adobe.commerce.cif" \
        -DarchetypeArtifactId="cif-project-archetype" \
        -DarchetypeVersion=${releaseVersion} \
        -DgroupId="com.adobe.commerce.cif" \
        -DartifactId="cif-sample-project" \
        -Dversion=${releaseVersion} \
        -Dpackage="com.adobe.commerce.cif" \
        -DappsFolderName="venia" \
        -DartifactName="Venia Demo Store" \
        -DcontentFolderName="venia" \
        -DpackageGroup="venia" \
        -DsiteName="Venia Demo Store" \
        -DoptionAemVersion=6.5.0 \
        -DoptionIncludeExamples=y \
        -DoptionEmbedConnector=n`);

    ci.dir('cif-sample-project', () => {
        ci.sh('mvn clean package');
    });
});


ci.stage('Update Project');
ci.dir('venia-store/cif-sample-project', () => {
    // Add dispatcher module to pom.xml
    let projectPom = fs.readFileSync('pom.xml', 'utf8');
    projectPom = projectPom.replace('<module>samplecontent</module>', '<module>samplecontent</module>\n        <module>dispatcher</module>')
    fs.writeFileSync('pom.xml', projectPom);

    // Change Magento root category id to 2
    const productsPomPath = 'samplecontent/src/main/content/jcr_root/content/venia/us/en/products/.content.xml';
    let productsContent = fs.readFileSync(productsPomPath, 'utf8');
    productsContent = productsContent.replace('magentoRootCategoryId="4"', 'magentoRootCategoryId="2"');
    fs.writeFileSync(productsPomPath, productsContent);
});


ci.stage('Deploy Project');
const gitRemote = `https://${encodeURIComponent(ci.env('AMS_GIT_USER'))}:${encodeURIComponent(ci.env('AMS_GIT_PASS'))}@${ci.env('AMS_REPO').slice("https://".length)}`;
ci.sh('mkdir -p ../ams');
ci.dir('../ams', () => {
    ci.sh(`git clone --depth 1 ${gitRemote}`, false, false);
    ci.dir('aemcifdemo2', () => {
        ci.sh('cp -R ../../repo/venia-store/cif-sample-project/* .');
        ci.sh('git add -A');
        ci.gitCredentials(gitRemote, () => {
            ci.gitImpersonate(ci.env('AMS_GIT_USER'), ci.env('AMS_GIT_USER'), () => {
                ci.sh(`git commit -m "CircleCI: Update to Archetype ${releaseVersion}"`);
                ci.sh('git push');
            });
        });
    });
});
