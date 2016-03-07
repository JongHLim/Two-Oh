<?php
 
/*
 * Following code will delete a inventory from table
 * A inventory is identified by inventory id (id)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id'])) {
    $id = $_POST['id'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    $con = $db->connect();
 
    // mysql update row with matched id
    $result = mysqli_query($con, "DELETE FROM inventory WHERE id = $id");
 
    // check if row deleted or not
    if (mysqli_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "inventory successfully deleted";
 
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
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>