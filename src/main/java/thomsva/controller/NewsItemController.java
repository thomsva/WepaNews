/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thomsva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import thomsva.repository.NewsItemRepository;


public class NewsItemController {
    
    @Autowired
    private NewsItemRepository newsItemRepository;
    
}