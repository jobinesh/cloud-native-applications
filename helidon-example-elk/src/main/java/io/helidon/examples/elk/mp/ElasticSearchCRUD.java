package io.helidon.examples.elk.mp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ElasticSearchCRUD {
    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String INDEX = "hr";
    private static final String TYPE = "department";

    /**
     * Implemented Singleton pattern here
     * so that there is just one connection at a time.
     *
     * @return RestHighLevelClient
     */
    public synchronized RestHighLevelClient makeConnection() {

        if (restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }

        return restHighLevelClient;
    }

    public synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    public IndexResponse insertDepartment(Department dept) throws IOException {
        dept.setId(UUID.randomUUID().toString());
        try {
            IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, dept.getId());
            indexRequest.source(new ObjectMapper().writeValueAsString(dept), XContentType.JSON);
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
            return indexResponse;
        } catch (ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex) {
            ex.getLocalizedMessage();
        }
        return null;
    }

    public Department updateDepartment(Department dept) {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, dept.getId())
                .fetchSource(true);    // Fetch Object after its update
        try {
            String deptJson = objectMapper.writeValueAsString(dept);
            updateRequest.doc(deptJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Department.class);
        } catch (JsonProcessingException e) {
            e.getMessage();
        } catch (java.io.IOException e) {
            e.getLocalizedMessage();
        }
        System.out.println("Unable to update department");
        return null;
    }

    private static Employee createEmployee(String firstName, String lastName, String sex) {

        Employee emp = new Employee(UUID.randomUUID().toString(), firstName, lastName, sex);
        return emp;
    }

    public List<Department> findDepartmentsByName(String name) throws IOException {
        List<Department> depts = new ArrayList<>();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", name)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(2)
                .maxExpansions(10);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(matchQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(5);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Department dept = new ObjectMapper().readValue(searchHit.getSourceAsString(), Department.class);
            depts.add(dept);
        }
        return depts;
    }

    public List<Department> findAllDepartments() throws IOException {
        List<Department> depts = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(5);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            Department dept = new ObjectMapper().readValue(searchHit.getSourceAsString(), Department.class);
            depts.add(dept);
        }
        return depts;
    }

    public static void main(String[] args) throws IOException {
        ElasticSearchCRUD elasticSearchCRUD = new ElasticSearchCRUD();
        elasticSearchCRUD.makeConnection();
        List<Department> depts = elasticSearchCRUD.findDepartmentsByName("IT");
        if (depts.isEmpty()) {
            Department dept = new Department();
            dept.setName("IT");
            dept.setLocation("FC");
            dept.addEmployee(createEmployee("J", "M", "Male"));
            dept.addEmployee(createEmployee("S", "M", "Male"));
            IndexResponse response = elasticSearchCRUD.insertDepartment(dept);
            System.out.println(response);
        } else {
            for (Department dept : depts) {
                dept.getEmployees().clear();
                dept.addEmployee(createEmployee("S", "P", "Male"));
                dept.addEmployee(createEmployee("S", "J", "Female"));

                elasticSearchCRUD.updateDepartment(dept);
            }
        }
        depts = elasticSearchCRUD.findAllDepartments();
        System.out.println("Depts read from elk "+ depts);
        elasticSearchCRUD.closeConnection();
    }
}
