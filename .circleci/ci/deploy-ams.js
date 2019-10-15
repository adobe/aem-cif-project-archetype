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


ci.sh('mvn help:evaluate -Dexpression=project.version -q -DforceStdout > version.txt');

// TODO: 
// 1.  Perform release


// 2. In parallel:
// Checkout tag and create sample project, use normal POM
// Deploy to Github Release artifacts

// 3. In parallel:
// Checkout tag and create sample project, use normal POM
// checkout cloudmanager repo
// Make adaptions and copy generated project into cloudmanager folder
// Git commit and push