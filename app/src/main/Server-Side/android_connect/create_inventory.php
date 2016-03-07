<?php
 
/*
 * Following code will create a new inventory row
 * All inventory details are read from HTTP Post Request
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
 
    // mysql inserting a new row
    $result = mysqli_query($con, "INSERT INTO inventory(ut_tag, check_in_date, check_out_date, machine_type, operating_system, checked_in) VALUES('$ut_tag', '$check_in_date', '$check_out_date', '$machine_type', '$operating_system', '$checked_in')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Inventory successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "An error occurred.";
 
        // echoing JSON response
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