<?php
require('../sihaty/ConnectDB.php');
    $result = $conn->query("SELECT COUNT(*) FROM rendezvous WHERE isConfirmed=0");
    while($cmp = $result->fetch(PDO::FETCH_ASSOC)){
        $donnee = $cmp["COUNT(*)"];
    }

    if($donnee > 0)
        echo '<center><div id="notification">'.$donnee.'</div></center>';
?>
