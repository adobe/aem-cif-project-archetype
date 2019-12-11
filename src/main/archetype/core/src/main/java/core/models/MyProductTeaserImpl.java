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
package ${package}.core.models;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import com.adobe.cq.commerce.core.components.models.productteaser.ProductTeaser;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductRetriever;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(adaptables = SlingHttpServletRequest.class, adapters = MyProductTeaser.class, resourceType = MyProductTeaserImpl.RESOURCE_TYPE)
public class MyProductTeaserImpl implements MyProductTeaser {

    protected static final String RESOURCE_TYPE = "venia/components/commerce/productteaser";

    @Self
    @Via(type = ResourceSuperType.class)
    private ProductTeaser productTeaser;

    @ScriptVariable
    private ValueMap properties;

    private AbstractProductRetriever productRetriever;

    private Boolean showBadge = false;

    @PostConstruct
    public void initModel() {
        productRetriever = productTeaser.getProductRetriever();

        boolean showBadgeProp = properties.get("badge", false);
        int maxAgeProp = properties.get("age", 0);

        if (productRetriever != null) {
            // Pass your custom partial query to the ProductRetriever. This class will
            // automatically take care of executing your query as soon
            // as you try to access any product property.
            productRetriever.extendProductQueryWith(p -> p.createdAt());

            // Custom code to calc the date difference of the product creation
            // compared to today
            if (showBadgeProp) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDate createdAt = LocalDate.parse(productRetriever.fetchProduct().getCreatedAt(), formatter);
                if (createdAt != null) {
                    Period period = Period.between(createdAt, LocalDate.now());
                    int age = period.getDays();
                    if (age < maxAgeProp) {
                        showBadge = true;
                    }
                }
            }
        }
    }

    @Override
    public Boolean isShowBadge() {
        return showBadge;
    }

    @Override
    public String getFormattedPrice() {
        return productTeaser.getFormattedPrice();
    }

    @Override
    public String getImage() {
        return productTeaser.getImage();
    }

    @Override
    public String getName() {
        return productTeaser.getName();
    }

    @Override
    public String getUrl() {
        return productTeaser.getUrl();
    }

    @Override
    public AbstractProductRetriever getProductRetriever() {
        return productRetriever;
    }
}