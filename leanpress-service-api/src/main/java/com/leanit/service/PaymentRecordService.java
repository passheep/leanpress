/**
 * Copyright (c) 2016-2019, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leanit.service;

import com.leanit.model.PaymentRecord;

import java.util.List;

public interface PaymentRecordService {

    /**
     * find model by primary key
     *
     * @param id
     * @return
     */
    public PaymentRecord findById(Object id);


    /**
     * find all model
     *
     * @return all <PaymentRecord
     */
    public List<PaymentRecord> findAll();


    /**
     * delete model by primary key
     *
     * @param id
     * @return success
     */
    public boolean deleteById(Object id);


    /**
     * delete model
     *
     * @param model
     * @return
     */
    public boolean delete(PaymentRecord model);


    /**
     * save model to database
     *
     * @param model
     * @return
     */
    public Object save(PaymentRecord model);


    /**
     * save or update model
     *
     * @param model
     * @return if save or update success
     */
    public Object saveOrUpdate(PaymentRecord model);


    /**
     * update data model
     *
     * @param model
     * @return
     */
    public boolean update(PaymentRecord model);

}