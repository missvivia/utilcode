package com.xyl.mmall.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netease.print.security.util.SecurityContextUtils;
import com.xyl.mmall.backend.facade.LeftNavigationFacade;

/**
 * Hello world!
 *
 */
@Controller
public class TestController
{
	@Autowired
	private LeftNavigationFacade leftNavigationFacade;
	
//    @RequestMapping(value ="/decorate", method = RequestMethod.GET)
//    public String decorate(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/brand/decorate";
//    }
//    @RequestMapping(value ="/decorate/custom", method = RequestMethod.GET)
//    public String custom(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/brand/custom";
//    }
//    @RequestMapping(value ="/product", method = RequestMethod.GET)
//    public String product(Model model){
//        return "pages/product/list";
//    }
//    @RequestMapping(value ="/product/list", method = RequestMethod.GET)
//    public String productList(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/product/list";
//    }    
   // @RequestMapping(value ="/product/edit", method = RequestMethod.GET)
   // public String productEdit(Model model){
   //     model.addAttribute("name", "aaa");
   //     return "pages/product/edit";
   // }
//    @RequestMapping(value ="/product/size", method = RequestMethod.GET)
//    public String productSize(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/product/size";
//    }
//   @RequestMapping(value ="/sell", method = RequestMethod.GET)
//   public String sell(Model model){
//       return "pages/sell/return";
//   }
//   @RequestMapping(value ="/sell/return", method = RequestMethod.GET)
//	@RequiresPermissions(value = { "sell:return" })
//   public String sellReturn(Model model){
//       model.addAttribute("name", "aaa");
//       model.addAttribute("pages", leftNavigationFacade.getLeftNavigationMenu(SecurityContextUtils.getUserId()));
//       return "pages/sell/return";
//   }

//   @RequestMapping(value ="/sell/detail", method = RequestMethod.GET)
//   public String sellDetail(Model model){
//       model.addAttribute("name", "aaa");
//       return "pages/sell/detail";
//   }
//   @RequestMapping(value ="/schedule/decorate", method = RequestMethod.GET)
//   public String zz(Model model){
//       model.addAttribute("name", "aaa");
//       return "pages/schedule/decorate";
//   }
//    @RequestMapping(value ="/image", method = RequestMethod.GET)
//    public String image(Model model){
//        return "pages/image/upload";
//    }
//    @RequestMapping(value ="/image/upload", method = RequestMethod.GET)
//    public String imageUpload(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/image/upload";
//    }
//    @RequestMapping(value ="/image/manage", method = RequestMethod.GET)
//    public String imageManage(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/image/manage";
//    }
//    @RequestMapping(value ="/image/category", method = RequestMethod.GET)
//    public String imageCategory(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/image/category";
//    }
//    @RequestMapping(value ="/coupon", method = RequestMethod.GET)
//    public String CouponIndex(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/coupon/index";
//    }
//    @RequestMapping(value ="/coupon/edit", method = RequestMethod.GET)
//    public String CouponEdit(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/coupon/edit";
//    }
//    @RequestMapping(value ="/message", method = RequestMethod.GET)
//    public String MessageIndex(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/message/index";
//    }
//    @RequestMapping(value ="/message/view", method = RequestMethod.GET)
//    public String MessageEdit(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/message/view";
//    }
//    @RequestMapping(value ="/activity", method = RequestMethod.GET)
//    public String ActivityIndex(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/activity/index";
//    }
//    @RequestMapping(value ="/activity/edit", method = RequestMethod.GET)
//    public String ActivityEdit(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/activity/edit";
//    }

    // @RequestMapping(value ="/po", method = RequestMethod.GET) // /schedule schedule/list.ftl
    // public String po(Model model){
    //     return "pages/po/polist";
    // }
    // @RequestMapping(value ="/po/polist", method = RequestMethod.GET)
    // public String poPolist(Model model){
    //     return "pages/po/polist";
    // }
    // @RequestMapping(value ="/po/schedule", method = RequestMethod.GET)  // /schedule/pages  schedule/pages
    // public String poSchedul(Model model){
    //     return "pages/po/schedulelist";
    // }
//    @RequestMapping(value ="/schedule/add", method = RequestMethod.GET) // /schedule/add?id=  
//    public String poAdd(Model model){
//        return "pages/schedule/add";
//    }
//    @RequestMapping(value ="/po/schedulelist", method = RequestMethod.GET)
//    public String poSchedulelist(Model model){
//        return "pages/po/schedulelist";
//    }
//    @RequestMapping(value ="/brand", method = RequestMethod.GET)
//    public String brand(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/brand/display";
//    }
//    @RequestMapping(value ="/brand/display", method = RequestMethod.GET)
//    public String brandDisplay(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/brand/display";
//    }
//
//    @RequestMapping(value ="/brand/create", method = RequestMethod.GET)
//    public String brandCreate(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/brand/create";
//    }

//    @RequestMapping(value ="/banner", method = RequestMethod.GET)
//    public String banner(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/banner/list";
//    }
//    @RequestMapping(value ="/po/blist", method = RequestMethod.GET)
//    public String bannerManager(Model model){
//        model.addAttribute("name", "aaa");
//        return "pages/banner/list";
//    }
   // @RequestMapping(value ="/product/helper", method = RequestMethod.GET)
   // public String helperList(Model model){
   //     model.addAttribute("name", "aaa");
   //     return "pages/product/helper";
   // }
   // @RequestMapping(value ="/product/viewhelper", method = RequestMethod.GET)
   // public String viewHelper(Model model){
   //     model.addAttribute("name", "aaa");
   //     return "pages/product/view.helper";
   // }
}
