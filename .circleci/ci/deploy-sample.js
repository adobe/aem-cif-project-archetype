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

ci.context();

ci.stage('Install Archetype');
ci.sh('mvn clean install');
const releaseVersion = ci.sh(`mvn help:evaluate -Dexpression=project.version -q -DforceStdout`, true).toString().trim();

ci.stage('Generate Sample Project');
ci.sh('mkdir -p venia-store');
ci.dir('venia-store', () => {
    ci.sh(`mvn archetype:generate -B \
        -DarchetypeGroupId="com.adobe.commerce.cif" \
        -DarchetypeArtifactId="cif-project-archetype" \
        -DarchetypeVersion=${releaseVersion} \
        -DgroupId="com.venia.cif" \
        -DartifactId="demo-store" \
        -Dversion=${releaseVersion} \
        -Dpackage="com.venia.cif" \
        -DappsFolderName="venia" \
        -DartifactName="Venia Demo Store" \
        -DcontentFolderName="venia" \
        -DpackageGroup="venia" \
        -DsiteName="Venia Demo Store" \
        -DoptionAemVersion=6.5.0 \
        -DoptionIncludeExamples=y \
        -DoptionEmbedConnector=y`);

    ci.dir('demo-store', () => {
        ci.sh('mvn clean package');
    });
});

ci.stage('Install GHR');
ci.sh('mkdir -p tmp');
ci.sh('curl -L https://github.com/tcnksm/ghr/releases/download/v0.12.1/ghr_v0.12.1_linux_amd64.tar.gz | tar xvz -C ./tmp');
ci.sh('mv tmp/**/ghr ./ghr');
ci.sh('chmod +x ghr');


ci.stage('Deploy Sample Project');
const allPackagePath = `venia-store/demo-store/all/target/demo-store.all-${releaseVersion}.zip`;
ci.sh(`./ghr -t ${ci.env('GITHUB_TOKEN')} \
    -u ${ci.env('CIRCLE_PROJECT_USERNAME')} \
    -r ${ci.env('CIRCLE_PROJECT_REPONAME')} \
    -c ${ci.env('CIRCLE_SHA1')} \
    -replace ${ci.env('CIRCLE_TAG')} ${allPackagePath}`);