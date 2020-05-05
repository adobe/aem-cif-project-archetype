/*******************************************************************************
 *
 *    Copyright 2020 Adobe. All rights reserved.
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

import com.adobe.cq.commerce.core.components.models.productteaser.ProductTeaser;
import com.adobe.cq.commerce.core.components.models.retriever.AbstractProductRetriever;
import com.adobe.cq.commerce.magento.graphql.ProductInterface;
import junit.framework.Assert;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class MyProductTeaserImplTest {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @InjectMocks
    private MyProductTeaserImpl underTest;

    @Mock
    private ProductTeaser productTeaser;

    @Mock
    private ValueMap properties;

    @Mock
    private AbstractProductRetriever productRetriever;

    @Mock
    private ProductInterface product;

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(productRetriever.fetchProduct()).thenReturn(product);
    }

    @Test
    void testShowBadge_noBadge() throws Exception {
        Mockito.when(properties.get("badge", false)).thenReturn(false);

        Assert.assertNotNull(underTest);
        Assert.assertFalse(underTest.isShowBadge());
    }

    @Test
    void testShowBadge_noAge() throws Exception {
        Mockito.when(properties.get("badge", false)).thenReturn(true);
        Mockito.when(properties.get("age", 0)).thenReturn(1);
        Mockito.when(product.getCreatedAt()).thenReturn("2020-01-01 00:00:00");

        Assert.assertNotNull(underTest);
        Assert.assertFalse(underTest.isShowBadge());
    }

    @Test
    void testShowBadge_badge() throws Exception {
        Mockito.when(properties.get("badge", false)).thenReturn(true);
        Mockito.when(properties.get("age", 0)).thenReturn(2);
        Mockito.when(product.getCreatedAt()).thenReturn(LocalDateTime.now().format(formatter));

        Assert.assertNotNull(underTest);
        Assert.assertTrue(underTest.isShowBadge());
    }

    @Test
    void testGetName() throws Exception {
        Mockito.when(productTeaser.getName()).thenReturn("TestName");
        Assert.assertEquals(productTeaser.getName(), underTest.getName());
    }

    @Test
    void testGetPriceRange() {
        Assert.assertEquals(productTeaser.getPriceRange(), underTest.getPriceRange());
    }

    @Test
    void testGetImage() {
        Mockito.when(productTeaser.getImage()).thenReturn("TestImage");
        Assert.assertEquals(productTeaser.getImage(), underTest.getImage());
    }

    @Test
    void testGetUrl() {
        Mockito.when(productTeaser.getUrl()).thenReturn("TestUrl");
        Assert.assertEquals(productTeaser.getUrl(), underTest.getUrl());
    }

    @Test
    void testGetSku() {
        Mockito.when(productTeaser.getSku()).thenReturn("TestSKU");
        Assert.assertEquals(productTeaser.getSku(), underTest.getSku());
    }

    @Test
    public void testGetCallToAction() {
        Mockito.when(productTeaser.getCallToAction()).thenReturn("TestCTA");
        Assert.assertEquals(productTeaser.getCallToAction(), underTest.getCallToAction());
    }

    @Test
    void testIsVirtualProduct() {
        Mockito.when(productTeaser.isVirtualProduct()).thenReturn(true);
        Assert.assertTrue(underTest.isVirtualProduct());

        Mockito.when(productTeaser.isVirtualProduct()).thenReturn(false);
        Assert.assertFalse(underTest.isVirtualProduct());
    }

    @Test
    void testGetProductRetriever() {
        Assert.assertEquals(productRetriever, underTest.getProductRetriever());
    }
}
