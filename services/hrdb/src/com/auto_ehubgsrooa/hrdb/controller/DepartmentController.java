/*Copyright (c) 2018-2019 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.auto_ehubgsrooa.hrdb.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.commons.wrapper.StringWrapper;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.runtime.file.manager.ExportedFileManager;
import com.wavemaker.runtime.file.model.Downloadable;
import com.wavemaker.runtime.security.xss.XssDisable;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.auto_ehubgsrooa.hrdb.Department;
import com.auto_ehubgsrooa.hrdb.Employee;
import com.auto_ehubgsrooa.hrdb.service.DepartmentService;


/**
 * Controller object for domain model class Department.
 * @see Department
 */
@RestController("hrdb.DepartmentController")
@Api(value = "DepartmentController", description = "Exposes APIs to work with Department resource.")
@RequestMapping("/hrdb/Department")
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
	@Qualifier("hrdb.DepartmentService")
	private DepartmentService departmentService;

	@Autowired
	private ExportedFileManager exportedFileManager;

	@ApiOperation(value = "Creates a new Department instance.")
    @RequestMapping(method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Department createDepartment(@RequestBody Department department) {
		LOGGER.debug("Create Department with information: {}" , department);

		department = departmentService.create(department);
		LOGGER.debug("Created Department with information: {}" , department);

	    return department;
	}

    @ApiOperation(value = "Returns the Department instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Department getDepartment(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting Department with id: {}" , id);

        Department foundDepartment = departmentService.getById(id);
        LOGGER.debug("Department details with id: {}" , foundDepartment);

        return foundDepartment;
    }

    @ApiOperation(value = "Updates the Department instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.PUT)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Department editDepartment(@PathVariable("id") Integer id, @RequestBody Department department) {
        LOGGER.debug("Editing Department with id: {}" , department.getDeptId());

        department.setDeptId(id);
        department = departmentService.update(department);
        LOGGER.debug("Department details with id: {}" , department);

        return department;
    }

    @ApiOperation(value = "Deletes the Department instance associated with the given id.")
    @RequestMapping(value = "/{id:.+}", method = RequestMethod.DELETE)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteDepartment(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting Department with id: {}" , id);

        Department deletedDepartment = departmentService.delete(id);

        return deletedDepartment != null;
    }

    @RequestMapping(value = "/deptCode/{deptCode}", method = RequestMethod.GET)
    @ApiOperation(value = "Returns the matching Department with given unique key values.")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Department getByDeptCode(@PathVariable("deptCode") String deptCode) {
        LOGGER.debug("Getting Department with uniques key DeptCode");
        return departmentService.getByDeptCode(deptCode);
    }

    /**
     * @deprecated Use {@link #findDepartments(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of Department instances matching the search criteria.")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Page<Department> searchDepartmentsByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Departments list by query filter:{}", (Object) queryFilters);
        return departmentService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Department instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @RequestMapping(method = RequestMethod.GET)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Department> findDepartments(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Departments list by filter:", query);
        return departmentService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Department instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @RequestMapping(value="/filter", method = RequestMethod.POST, consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Page<Department> filterDepartments(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Departments list by filter", query);
        return departmentService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
    @RequestMapping(value = "/export/{exportType}", method = {RequestMethod.GET,  RequestMethod.POST}, produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public Downloadable exportDepartments(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return departmentService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @RequestMapping(value = "/export", method = {RequestMethod.POST}, consumes = "application/json")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @XssDisable
    public StringWrapper exportDepartmentsAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = Department.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> departmentService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }

	@ApiOperation(value = "Returns the total count of Department instances matching the optional query (q) request param. If query string is too big to fit in GET request's query param, use POST method with application/x-www-form-urlencoded format.")
	@RequestMapping(value = "/count", method = {RequestMethod.GET, RequestMethod.POST})
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	@XssDisable
	public Long countDepartments( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting Departments");
		return departmentService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@RequestMapping(value = "/aggregations", method = RequestMethod.POST)
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	@XssDisable
	public Page<Map<String, Object>> getDepartmentAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return departmentService.getAggregatedValues(aggregationInfo, pageable);
    }

    @RequestMapping(value="/{id:.+}/employees", method=RequestMethod.GET)
    @ApiOperation(value = "Gets the employees instance associated with the given id.")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Employee> findAssociatedEmployees(@PathVariable("id") Integer id, Pageable pageable) {

        LOGGER.debug("Fetching all associated employees");
        return departmentService.findAssociatedEmployees(id, pageable);
    }

    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service DepartmentService instance
	 */
	protected void setDepartmentService(DepartmentService service) {
		this.departmentService = service;
	}

}