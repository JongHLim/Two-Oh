<?php
 
/*
 * Following code will update a inventory information
 * A inventory is identified by inventory id (id)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['ut_tag']) && isset($_POST['check_in_date']) &&
    isset($_POST['check_out_date']) && isset($_POST['machine_type']) &&
    isset($_POST['operating_system']) && isset($_POST['checked_in'])) {
 
    $ut_tag = $_POST['ut_tag'];
    $check_in_date = $_POST['check_in_date'];
    $check_out_date = $_POST['check_out_date'];
    $machine_type = $_POST['machine_type'];
    $operating_system = $_POST['operating_system'];
    $checked_in = $_POST['checked_in'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    $con = $db->connect();
 
    // mysql update row with matched id
    $result = mysql_query($con, "UPDATE inventory SET ut_tag = '$ut_tag', check_in_date = '$check_in_date', check_out_date = '$check_out_date', machine_type = '$machine_type', operating_system = '$operating_system', checked_in = '$checked_in' WHERE id = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "inventory successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>