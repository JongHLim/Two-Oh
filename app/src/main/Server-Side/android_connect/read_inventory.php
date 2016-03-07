<?php
 
/*
 * Following code will get single inventory details
 * A inventory is identified by inventory id (id)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
$con = $db->connect();
 
// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    // get a inventory from inventory table
    $result = mysqli_query($con, "SELECT *FROM inventory WHERE id = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $inventory = array();
            $inventory["id"] = $result["id"];
            $inventory["ut_tag"] = $result["ut_tag"];
            $inventory["check_in_date"] = $result["check_in_date"];
            $inventory["check_out_date"] = $result["check_out_date"];
            $inventory["machine_type"] = $result["machine_type"];
            $inventory["operating_system"] = $result["operating_system"];
            $inventory["checked_in"] = $result["checked_in"];
            $inventory["created_at"] = $result["created_at"];
            $inventory["updated_at"] = $result["updated_at"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["inventory"] = array();
 
            array_push($response["inventory"], $inventory);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no inventory found
            $response["success"] = 0;
            $response["message"] = "No inventory found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no inventory found
        $response["success"] = 0;
        $response["message"] = "No inventory found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>