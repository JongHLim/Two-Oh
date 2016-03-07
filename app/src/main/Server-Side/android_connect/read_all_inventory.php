<?php
 
/*
 * Following code will list all the inventory
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
 
// get all inventory from inventory table
$result = mysqli_query($con, "SELECT *FROM inventory") or die(mysql_error());
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    // inventory node
    $response["inventory"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $inventory["id"] = $row["id"];
        $inventory["ut_tag"] = $row["ut_tag"];
        $inventory["check_in_date"] = $row["check_in_date"];
        $inventory["check_out_date"] = $row["check_out_date"];
        $inventory["machine_type"] = $row["machine_type"];
        $inventory["operating_system"] = $row["operating_system"];
        $inventory["checked_in"] = $row["checked_in"];
        $inventory["created_at"] = $row["created_at"];
        $inventory["updated_at"] = $row["updated_at"];
 
        // push single inventory into final response array
        array_push($response["inventory"], $inventory);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no inventory found
    $response["success"] = 0;
    $response["message"] = "No inventory found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>