<?php
require('../sihaty/ConnectDB.php');
$id = $_POST["user_id"];
	$conn->exec("UPDATE patients SET isConfirmed=1 WHERE id=$id");
?>

<script>
	goBack();
	function goBack(){
		window.history.back();
	}
</script>