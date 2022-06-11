package com.gitegg.service.extension.justauth.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitegg.platform.base.constant.GitEggConstant;
import com.gitegg.platform.base.result.PageResult;
import com.gitegg.platform.base.result.Result;
import com.gitegg.platform.base.dto.CheckExistDTO;
import com.gitegg.service.extension.justauth.entity.*;
import com.gitegg.service.extension.justauth.dto.*;

import com.gitegg.service.extension.justauth.service.IJustAuthConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import com.alibaba.excel.EasyExcel;
import com.gitegg.platform.base.util.BeanCopierUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
* <p>
* 租户第三方登录功能配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-16
*/
@RestController
@RequestMapping("/extension/justauth/config")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthConfigController|租户第三方登录功能配置表前端控制器")
@RefreshScope
public class JustAuthConfigController {

    private final IJustAuthConfigService justAuthConfigService;

    /**
    * 查询租户第三方登录功能配置表列表
    *
    * @param queryJustAuthConfigDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录功能配置表列表")
    public PageResult<JustAuthConfigDTO> list(QueryJustAuthConfigDTO queryJustAuthConfigDTO, Page<JustAuthConfigDTO> page) {
        Page<JustAuthConfigDTO> pageJustAuthConfig = justAuthConfigService.queryJustAuthConfigList(page, queryJustAuthConfigDTO);
        return PageResult.data(pageJustAuthConfig.getTotal(), pageJustAuthConfig.getRecords());
    }

    /**
    * 查询租户第三方登录功能配置表详情
    *
    * @param queryJustAuthConfigDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录功能配置表详情")
    public Result<?> query(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        JustAuthConfigDTO justAuthConfigDTO = justAuthConfigService.queryJustAuthConfig(queryJustAuthConfigDTO);
        return Result.data(justAuthConfigDTO);
    }

    /**
    * 添加租户第三方登录功能配置表
    *
    * @param justAuthConfig
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录功能配置表")
    public Result<?> create(@RequestBody CreateJustAuthConfigDTO justAuthConfig) {
        boolean result = justAuthConfigService.createJustAuthConfig(justAuthConfig);
        return Result.result(result);
    }

    /**
    * 修改租户第三方登录功能配置表
    *
    * @param justAuthConfig
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录功能配置表")
    public Result<?> update(@RequestBody UpdateJustAuthConfigDTO justAuthConfig) {
        boolean result = justAuthConfigService.updateJustAuthConfig(justAuthConfig);
        return Result.result(result);
    }

    /**
    * 删除租户第三方登录功能配置表
    *
    * @param justAuthConfigId
    * @return
    */
    @PostMapping("/delete/{justAuthConfigId}")
    @ApiOperation(value = "删除租户第三方登录功能配置表")
    @ApiImplicitParam(paramType = "path", name = "justAuthConfigId", value = "租户第三方登录功能配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("justAuthConfigId") Long justAuthConfigId) {
        if (null == justAuthConfigId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthConfigService.deleteJustAuthConfig(justAuthConfigId);
        return Result.result(result);
    }

    /**
    * 批量删除租户第三方登录功能配置表
    *
    * @param justAuthConfigIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录功能配置表")
    @ApiImplicitParam(name = "justAuthConfigIds", value = "租户第三方登录功能配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> justAuthConfigIds) {
        if (CollectionUtils.isEmpty(justAuthConfigIds)) {
            return Result.error("租户第三方登录功能配置表ID列表不能为空");
        }
        boolean result = justAuthConfigService.batchDeleteJustAuthConfig(justAuthConfigIds);
        return Result.result(result);
    }
     /**
     * 修改租户第三方登录功能配置表状态
     *
     * @param justAuthConfigId
     * @param status
     * @return
     */
     @PostMapping("/status/{justAuthConfigId}/{status}")
     @ApiOperation(value = "修改租户第三方登录功能配置表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "justAuthConfigId", value = "租户第三方登录功能配置表ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "status", value = "租户第三方登录功能配置表状态", required = true, dataType = "Integer", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("justAuthConfigId") Long justAuthConfigId,
         @PathVariable("status") Integer status) {

         if (null == justAuthConfigId || StringUtils.isEmpty(status)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateJustAuthConfigDTO justAuthConfig = new UpdateJustAuthConfigDTO();
         justAuthConfig.setId(justAuthConfigId);
         justAuthConfig.setStatus(status);
         boolean result = justAuthConfigService.updateJustAuthConfig(justAuthConfig);
         return Result.result(result);
     }


    /**
    * 批量导出租户第三方登录功能配置表数据
    * @param response
    * @param queryJustAuthConfigDTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QueryJustAuthConfigDTO queryJustAuthConfigDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<JustAuthConfigDTO> justAuthConfigList = justAuthConfigService.queryJustAuthConfigList(queryJustAuthConfigDTO);
        List<JustAuthConfigExport> justAuthConfigExportList = new ArrayList<>();
        for (JustAuthConfigDTO justAuthConfigDTO : justAuthConfigList) {
           JustAuthConfigExport justAuthConfigExport = BeanCopierUtils.copyByClass(justAuthConfigDTO, JustAuthConfigExport.class);
           justAuthConfigExportList.add(justAuthConfigExport);
        }
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthConfigExport.class).sheet(sheetName).doWrite(justAuthConfigExportList);
    }

    /**
    * 批量上传租户第三方登录功能配置表数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<JustAuthConfigImport> justAuthConfigImportList =  EasyExcel.read(file.getInputStream(), JustAuthConfigImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(justAuthConfigImportList))
        {
            List<JustAuthConfig> justAuthConfigList = new ArrayList<>();
            justAuthConfigImportList.stream().forEach(justAuthConfigImport-> {
               justAuthConfigList.add(BeanCopierUtils.copyByClass(justAuthConfigImport, JustAuthConfig.class));
            });
            justAuthConfigService.saveBatch(justAuthConfigList);
        }
        return Result.success();
    }

    /**
    * 下载租户第三方登录功能配置表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录功能配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "租户第三方登录功能配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthConfigImport.class).sheet(sheetName).doWrite(null);
    }
 }
