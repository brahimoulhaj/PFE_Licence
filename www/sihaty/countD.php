<?php
require('../sihaty/ConnectDB.php');
    $result = $conn->query("SELECT COUNT(*) FROM dossiers");
    while($cmp = $result->fetch(PDO::FETCH_ASSOC)){
        $donnee = $cmp["COUNT(*)"];
    }

    echo '<font style="font-size:10px; font-weight:normal">Il existe '.$donnee.' dossiers</font>';
?>