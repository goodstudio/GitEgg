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

import com.gitegg.service.extension.justauth.service.IJustAuthSourceService;

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
* 租户第三方登录信息配置表 前端控制器
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@RestController
@RequestMapping("/extension/justauth/source")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Api(value = "JustAuthSourceController|租户第三方登录信息配置表前端控制器")
@RefreshScope
public class JustAuthSourceController {

    private final IJustAuthSourceService justAuthSourceService;

    /**
    * 查询租户第三方登录信息配置表列表
    *
    * @param queryJustAuthSourceDTO
    * @param page
    * @return
    */
    @GetMapping("/list")
    @ApiOperation(value = "查询租户第三方登录信息配置表列表")
    public PageResult<JustAuthSourceDTO> list(QueryJustAuthSourceDTO queryJustAuthSourceDTO, Page<JustAuthSourceDTO> page) {
        Page<JustAuthSourceDTO> pageJustAuthSource = justAuthSourceService.queryJustAuthSourceList(page, queryJustAuthSourceDTO);
        return PageResult.data(pageJustAuthSource.getTotal(), pageJustAuthSource.getRecords());
    }

    /**
    * 查询租户第三方登录信息配置表详情
    *
    * @param queryJustAuthSourceDTO
    * @return
    */
    @GetMapping("/query")
    @ApiOperation(value = "查询租户第三方登录信息配置表详情")
    public Result<?> query(QueryJustAuthSourceDTO queryJustAuthSourceDTO) {
        JustAuthSourceDTO justAuthSourceDTO = justAuthSourceService.queryJustAuthSource(queryJustAuthSourceDTO);
        return Result.data(justAuthSourceDTO);
    }

    /**
    * 添加租户第三方登录信息配置表
    *
    * @param justAuthSource
    * @return
    */
    @PostMapping("/create")
    @ApiOperation(value = "添加租户第三方登录信息配置表")
    public Result<?> create(@RequestBody CreateJustAuthSourceDTO justAuthSource) {
        boolean result = justAuthSourceService.createJustAuthSource(justAuthSource);
        return Result.result(result);
    }

    /**
    * 修改租户第三方登录信息配置表
    *
    * @param justAuthSource
    * @return
    */
    @PostMapping("/update")
    @ApiOperation(value = "更新租户第三方登录信息配置表")
    public Result<?> update(@RequestBody UpdateJustAuthSourceDTO justAuthSource) {
        boolean result = justAuthSourceService.updateJustAuthSource(justAuthSource);
        return Result.result(result);
    }

    /**
    * 删除租户第三方登录信息配置表
    *
    * @param justAuthSourceId
    * @return
    */
    @PostMapping("/delete/{justAuthSourceId}")
    @ApiOperation(value = "删除租户第三方登录信息配置表")
    @ApiImplicitParam(paramType = "path", name = "justAuthSourceId", value = "租户第三方登录信息配置表ID", required = true, dataType = "Long")
    public Result<?> delete(@PathVariable("justAuthSourceId") Long justAuthSourceId) {
        if (null == justAuthSourceId) {
            return Result.error("ID不能为空");
        }
        boolean result = justAuthSourceService.deleteJustAuthSource(justAuthSourceId);
        return Result.result(result);
    }

    /**
    * 批量删除租户第三方登录信息配置表
    *
    * @param justAuthSourceIds
    * @return
    */
    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除租户第三方登录信息配置表")
    @ApiImplicitParam(name = "justAuthSourceIds", value = "租户第三方登录信息配置表ID列表", required = true, dataType = "List")
    public Result<?> batchDelete(@RequestBody List<Long> justAuthSourceIds) {
        if (CollectionUtils.isEmpty(justAuthSourceIds)) {
            return Result.error("租户第三方登录信息配置表ID列表不能为空");
        }
        boolean result = justAuthSourceService.batchDeleteJustAuthSource(justAuthSourceIds);
        return Result.result(result);
    }
     /**
     * 修改租户第三方登录信息配置表状态
     *
     * @param justAuthSourceId
     * @param status
     * @return
     */
     @PostMapping("/status/{justAuthSourceId}/{status}")
     @ApiOperation(value = "修改租户第三方登录信息配置表状态")
     @ApiImplicitParams({
     @ApiImplicitParam(name = "justAuthSourceId", value = "租户第三方登录信息配置表ID", required = true, dataType = "Long", paramType = "path"),
     @ApiImplicitParam(name = "status", value = "租户第三方登录信息配置表状态", required = true, dataType = "Integer", paramType = "path") })
     public Result<?> updateStatus(@PathVariable("justAuthSourceId") Long justAuthSourceId,
         @PathVariable("status") Integer status) {

         if (null == justAuthSourceId || StringUtils.isEmpty(status)) {
           return Result.error("ID和状态不能为空");
         }
         UpdateJustAuthSourceDTO justAuthSource = new UpdateJustAuthSourceDTO();
         justAuthSource.setId(justAuthSourceId);
         justAuthSource.setStatus(status);
         boolean result = justAuthSourceService.updateJustAuthSource(justAuthSource);
         return Result.result(result);
     }


    /**
    * 批量导出租户第三方登录信息配置表数据
    * @param response
    * @param queryJustAuthSourceDTO
    * @throws IOException
    */
    @GetMapping("/download")
    public void download(HttpServletResponse response, QueryJustAuthSourceDTO queryJustAuthSourceDTO) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录信息配置表数据列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<JustAuthSourceDTO> justAuthSourceList = justAuthSourceService.queryJustAuthSourceList(queryJustAuthSourceDTO);
        List<JustAuthSourceExport> justAuthSourceExportList = new ArrayList<>();
        for (JustAuthSourceDTO justAuthSourceDTO : justAuthSourceList) {
           JustAuthSourceExport justAuthSourceExport = BeanCopierUtils.copyByClass(justAuthSourceDTO, JustAuthSourceExport.class);
           justAuthSourceExportList.add(justAuthSourceExport);
        }
        String sheetName = "租户第三方登录信息配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthSourceExport.class).sheet(sheetName).doWrite(justAuthSourceExportList);
    }

    /**
    * 批量上传租户第三方登录信息配置表数据
    * @param file
    * @return
    * @throws IOException
    */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("uploadFile") MultipartFile file) throws IOException {
    List<JustAuthSourceImport> justAuthSourceImportList =  EasyExcel.read(file.getInputStream(), JustAuthSourceImport.class, null).sheet().doReadSync();
        if (!CollectionUtils.isEmpty(justAuthSourceImportList))
        {
            List<JustAuthSource> justAuthSourceList = new ArrayList<>();
            justAuthSourceImportList.stream().forEach(justAuthSourceImport-> {
               justAuthSourceList.add(BeanCopierUtils.copyByClass(justAuthSourceImport, JustAuthSource.class));
            });
            justAuthSourceService.saveBatch(justAuthSourceList);
        }
        return Result.success();
    }

    /**
    * 下载租户第三方登录信息配置表数据导入模板
    * @param response
    * @throws IOException
    */
    @GetMapping("/download/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("租户第三方登录信息配置表数据导入模板", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        String sheetName = "租户第三方登录信息配置表数据列表";
        EasyExcel.write(response.getOutputStream(), JustAuthSourceImport.class).sheet(sheetName).doWrite(null);
    }
 }
