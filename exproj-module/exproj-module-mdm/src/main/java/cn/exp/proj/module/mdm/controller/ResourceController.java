package cn.exp.proj.module.mdm.controller;

import cn.exp.proj.common.web.constant.IWebConstant;
import cn.exp.proj.common.web.response.WebResult;
import cn.exp.proj.module.mdm.service.IResourceService;
import cn.exp.proj.module.mdm.vo.ResourceVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(IWebConstant.REST_WEB_URL + "/resource")
public class ResourceController {
    private IResourceService resourceService;

    @GetMapping("/{id}")
    public WebResult<ResourceVo> getById(@PathVariable("id") Long id) {
        return WebResult.suceess(resourceService.getById(id));
    }

    @GetMapping("/list")
    public WebResult<List<ResourceVo>> list(@RequestBody ResourceVo resourceVo) {
        return WebResult.suceess(resourceService.list(resourceVo));
    }

    @PostMapping
    public WebResult<ResourceVo> save(@RequestBody ResourceVo resourceVo) {
        return WebResult.suceess(resourceService.save(resourceVo));
    }

    @PutMapping
    public WebResult<ResourceVo> update(@RequestBody ResourceVo resourceVo) {
        return WebResult.suceess(resourceService.update(resourceVo));
    }

    @DeleteMapping("/{id}")
    public WebResult<ResourceVo> delete(@PathVariable("id") Long id) {
        return WebResult.suceess(resourceService.delete(id));
    }
}
